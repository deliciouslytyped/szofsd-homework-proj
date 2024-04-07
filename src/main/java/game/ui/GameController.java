package game.ui;

import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import lombok.extern.slf4j.Slf4j;

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

  // TODO StageController is probably badly written and this will result
  //in waiting for a window to initiailze stuff, but since this is a small program it's fine for now
  protected void myInitialize() {
    //TODO isnt there a way to do this without this?
    renderArea.heightProperty().bind(renderArea.getScene().heightProperty());
    renderArea.widthProperty().bind(renderArea.getScene().widthProperty());

    // TODO do this nicer
    //TODO hack
    var sceneSet = Scene3D.prepareScene(renderArea, true);
    var mapSet = Scene3D.prepareMap();
    camera = sceneSet.camera;
    sceneRoot = sceneSet.root;
    player = mapSet.player;
    mapRoot = mapSet.root;

    sceneRoot.getChildren().add(new Marker());

    ////TODO the camera is off center for some reason
    //camera.translateXProperty().bind(stage.widthProperty().multiply(-1/2.0));
    //camera.translateYProperty().bind(stage.heightProperty().multiply(-1/2.0));

    //root.getChildren().add(new Marker());
    sceneRoot.getChildren().add(mapRoot);

    ga = new GridAnimator(player);

    setUpInteractivity();
  }

  // TODO make sure this is done properly
  private void setUpInteractivity(){
    renderArea.setFocusTraversable(true);

    renderArea.setOnKeyPressed(k -> {
      log.trace("key pressed: {}", k);
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

        case D -> { ga.animateUp(); } // had to relabel these
        case A -> { ga.animateDown(); }
        case W -> { ga.animateLeft(); }
        case S -> { ga.animateRight(); }
      };
    });

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

    renderArea.requestFocus();
  }


}

