package game.ui;

import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

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

    //root.getChildren().add(new Marker());
    sceneRoot.getChildren().add(mapRoot);

    //TODO the camera is off center for some reason
    camera.translateXProperty().bind(stage.widthProperty().multiply(-1/2.0));
    camera.translateYProperty().bind(stage.heightProperty().multiply(-1/2.0));

    IGridAnimator ga = new GridAnimator(player);

    setUpInteractivity();
  }

  // TODO make sure this is done properly
  private void setUpInteractivity(){
    sceneRoot.setFocusTraversable(true);

    sceneRoot.setOnKeyPressed(k -> {
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

        case D -> { ga.animateUp(); } // had to relabel these
        case A -> { ga.animateDown(); }
        case W -> { ga.animateLeft(); }
        case S -> { ga.animateRight(); }
      };
    });

    //TODO this doesn't work nicely
    sceneRoot.setOnMouseDragged(e -> {
      //if (e.getEventType().) {
        //TODO fix and use transforms instead of rotobject
        //mapRoot.absrot(Math.min(Math.max(e.getY(), 0), 180), 0, e.getX());
      //}
    });

    sceneRoot.requestFocus();
  }


}

