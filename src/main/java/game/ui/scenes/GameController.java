package game.ui;

import lombok.extern.slf4j.Slf4j;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import game.model.GameModel;
import game.model.Map;
import game.model.Direction;
import game.model.Position;

import java.util.function.Consumer;


//TODO ok how does this even work, isnt log supposed to be inherited?
@Slf4j
public class GameController extends StageController {
  @FXML
  private SubScene renderArea;

  private Camera camera;
  private Player player;
  private Group sceneRoot;
  private Group mapRoot;
  private IGridAnimator ga;

  private ObjectProperty<GameModel> model =  new SimpleObjectProperty<>();

  // TODO StageController is probably badly written and this will result
  //in waiting for a window to initiailze stuff, but since this is a small program it's fine for now
  protected void myInitialize() {
    //TODO isnt there a way to do this without this?
    renderArea.heightProperty().bind(renderArea.getScene().heightProperty());
    renderArea.widthProperty().bind(renderArea.getScene().widthProperty());

    initGame();
  }

  protected void initGame(){
    ///TODO wrong/awkward also needs to be a bind / listener
    model.addListener((observable1, oldModel, newModel) -> {
      log.trace("Loaded new model");
      newModel.mapProperty().addListener((observable2, oldValue, newValue) -> {
       log.trace("Loaded new map");
       var mapSet = Scene3D.prepareMap(newValue);
       player = mapSet.player;
       var newMapRoot = mapSet.root;
       sceneRoot.getChildren().remove(mapRoot);
       mapRoot = newMapRoot;
       sceneRoot.getChildren().add(newMapRoot);

       //TODO I don't know how to reset the property properly otherwise, the change event doesnt get fired if it doesnt actually change
       newModel.initPlayerPosition(null);
       newModel.playerProperty().getValue().reset();
       // TODO instead of this, somehow fire events for the directions passed to move

       Consumer<Position> afterAnimation = (newPos) -> {
         var v = newModel.playerProperty().getValue().activeFaceProperty().getValue();
         log.trace("set texture to {}", v);
         player.cube.activeSide.setValue(v);
         //TODO this is redundant but good for sanity check?
         player.setPosition(newPos);
       };

       newModel.positionProperty().addListener((observable3, oldPos, newPos) -> {
         if (newPos == null) { return; };
         if (oldPos == null){
           //TODO this player parent thing is confusing, i already messed myself up
           player.setPosition(newPos);
         } else {
           //TODO technically not necessary to re-set the positions here but its a sanity check
           // set the side in the texture after animation finishes, probably move this to gridanimator
           //TODO having to pass the arg in stupid like this sucks
           switch (Direction.of(newPos.row()-oldPos.row(), newPos.col()-oldPos.col())) {
             case UP -> { ga.animateUp(afterAnimation, newPos); } // NOTE the new face textuer needs to be set after animation is finished
             case DOWN -> { ga.animateDown(afterAnimation, newPos); } // NOTE the new face textuer needs to be set after animation is finished
             case LEFT -> { ga.animateLeft(afterAnimation, newPos); } // NOTE the new face textuer needs to be set after animation is finished
             case RIGHT -> { ga.animateRight(afterAnimation, newPos); } // NOTE the new face textuer needs to be set after animation is finished
           };
         }
       });

       // TODO Pass by value semantics means we have to redo these.
       // What about deleting the old objects still hanging around?

       newModel.initPlayerPosition(new Position(1,5));

       log.trace("bound animator to player.");
       ga = new GridAnimator(player);

       log.trace("Set up player interactivity bindings.");
       // TODO this is a hack until i fix the multiple event handlers issue
       bindPlayer(ga, newModel);
       /*
       bindPlayer(ga);
       bindWorld(sceneRoot);
       //TODO the answer is onkeypressed is set to one value, dont have multiple handlers
       //TODO this shouldn't be here but for some reason the player controls arent working and I need to figure out why
       setUpInteractivity();
       */
       renderArea.setFocusTraversable(true);
       renderArea.requestFocus();
     });
    });

    // TODO do this nicer
    //TODO hack
    var sceneSet = Scene3D.prepareScene(renderArea, true);
    camera = sceneSet.camera;
    sceneRoot = sceneSet.root;

    // NOTE we have to do this after sceneRoot is set so that the above listener doesn't fail.
    // Is there any good way to impose dependency constraints on these events?
    //TODO this doesnt go here
    var gm = new GameModel();
    model.setValue(gm);
    gm.setMap();

    sceneRoot.getChildren().add(new Marker(Color.GREEN));

    ////TODO the camera is off center for some reason
    //TODO need to use thi if perspective camera
    //camera.translateXProperty().bind(stage.widthProperty().multiply(-1/2.0));
    //camera.translateYProperty().bind(stage.heightProperty().multiply(-1/2.0));
  }

  private void setUpInteractivity(){
    bindScene();
    renderArea.setFocusTraversable(true);
    renderArea.requestFocus();
  }

  // TODO make sure this is done properly
  // TODO not sure if there is a way to do this without passing the argument,
  //   the lambda capturing object refernce by value is necessitating this
  private void bindPlayer(IGridAnimator ga, GameModel model){
    renderArea.setOnKeyPressed(k -> {
      log.trace("bindplayer key pressed: {}", k);
      switch (k.getCode()) {
        case W -> { model.tryMove(Direction.UP); } // TODO had to relabel these
        case S -> { model.tryMove(Direction.DOWN); }
        case A -> { model.tryMove(Direction.LEFT); }
        case D -> { model.tryMove(Direction.RIGHT); }

        // TODO event handler hacks
        case LEFT -> { camera.setTranslateX(camera.translateXProperty().add(10).getValue()); }
        case RIGHT -> { camera.setTranslateX(camera.translateXProperty().add(-10).getValue()); }
        case DOWN -> { camera.setTranslateY(camera.translateYProperty().add(10).getValue()); }
        case UP -> { camera.setTranslateY(camera.translateYProperty().add(-10).getValue()); }
        case NUMPAD8 -> { camera.setTranslateZ(camera.getTranslateZ() + 10); }
        case NUMPAD2 -> { camera.setTranslateZ(camera.getTranslateZ() - 10); }
        case Q -> { System.exit(0); }
        case R -> { model.debugFireMap(); } // Trigger world reinit
      };
    });
  }

  // TODO same as bindPlayer, not sure if there is a way to do this without passing the argument,
  //   the lambda capturing object refernce by value is necessitating this
  private void bindWorld(Group sceneRoot){
    //TODO this doesn't work nicely
    renderArea.setOnMouseDragged(e -> {
      log.trace("rotating");
      Rotate a = new Rotate(e.getY(), Rotate.X_AXIS);
      Rotate b = new Rotate(e.getX(), Rotate.Y_AXIS);
      sceneRoot.getTransforms().clear();
      sceneRoot.getTransforms().addAll(a, b);
      //if (e.getEventType().) {
        //TODO fix and use transforms instead of rotobject
        //mapRoot.absrot(Math.min(Math.max(e.getY(), 0), 180), 0, e.getX());
      //}
    });
  }

  // TODO make sure this is done properly
  private void bindScene(){
    renderArea.setOnKeyPressed(k -> {
      log.trace("bindscene key pressed: {}", k);
      // TODO hack
      camera.translateXProperty().unbind();
      camera.translateYProperty().unbind();
      switch (k.getCode()) {
        case LEFT -> { camera.setTranslateX(camera.translateXProperty().add(10).getValue()); }
        case RIGHT -> { camera.setTranslateX(camera.translateXProperty().add(-10).getValue()); }
        case DOWN -> { camera.setTranslateY(camera.translateYProperty().add(10).getValue()); }
        case UP -> { camera.setTranslateY(camera.translateYProperty().add(-10).getValue()); }
        case NUMPAD8 -> { camera.setTranslateZ(camera.getTranslateZ() + 10); }
        case NUMPAD2 -> { camera.setTranslateZ(camera.getTranslateZ() - 10); }
        case Q -> { System.exit(0); }
        case R -> { model.getValue().debugFireMap(); } // Trigger world reinit

        // TODO Rotation is till all kinds of screwed up in this framework...
/*
        case H -> {
          Rotate rot = Rotate(0, -5, 0);
          rot = mapRoot.getTransforms().get(0).createConcatenation(rot)
          mapRoot.getTransforms().set(0, rot);
          }
        case F -> {
          mapRoot.setRotationAxis(Rotate.Y_AXIS);
          mapRoot.setRotate(mapRoot.getRotate() + 5);
          }
        case T -> {
          mapRoot.setRotationAxis(Rotate.X_AXIS);
          mapRoot.setRotate(mapRoot.getRotate() - 5);
          }
        case G -> {
          mapRoot.setRotationAxis(Rotate.X_AXIS);
          mapRoot.setRotate(mapRoot.getRotate() + 5);
          }
*/
      };
    });
  }


}

