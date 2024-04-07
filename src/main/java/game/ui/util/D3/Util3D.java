package game.ui;

import lombok.extern.slf4j.Slf4j;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.ParallelCamera;

@Slf4j
public class Util3D {

  public static boolean check3DSupport(){
    // Source: https://jenkov.com/tutorials/javafx/3d.html
    boolean is3DSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
    if(!is3DSupported) {
      // TODO test this
      log.error("""
        Sorry, 3D is not supported in JavaFX on this platform.
        You can try checking the developer documentation for information on how to
        attempt to forcefully enable the GPU backend with the `prism.forceGPU` property.
        """);
      return false;
      }
    return true;
  }

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
