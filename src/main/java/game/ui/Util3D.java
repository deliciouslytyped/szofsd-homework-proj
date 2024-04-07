package game.ui;

import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.ParallelCamera;

public class Util3D {
  public static Camera mkPerspectiveCam(){
    Camera camera = new PerspectiveCamera(true);
    camera.setTranslateZ(-1500);
    camera.setFarClip(6000);
    camera.setNearClip(0.01);
    return camera;
  }

  public static Camera mkIsoCam(){ // TODO why does clipping behave weird?
    Camera camera = new ParallelCamera();
    return camera;
  }

}
