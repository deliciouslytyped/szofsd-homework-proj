package game.ui;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

import game.ui.SceneStateMachine;

@Slf4j
public class GameApplication extends Application {

  private SceneStateMachine ssm;

  // TODO: Why is this marked NonNull in the examples?
  // Are there situations where this can be null? Does anything other than JavaFX call start?
  // TODO: Any specific reason for marking the stage argument final in examples?
  @Override
  public void start(@NonNull Stage stage) throws IOException {
    // Source: https://jenkov.com/tutorials/javafx/3d.html
    boolean is3DSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
    if(!is3DSupported) {
      // TODO test this
      log.error("""
        Sorry, 3D is not supported in JavaFX on this platform.
        You can try checking the developer documentation for information on how to
        attempt to forcefully enable the GPU backend with the `prism.forceGPU` property.
        """);
      return;
      }

    ssm = new SceneStateMachine(stage);
    stage.setUserData(UIState.builder()
            .ssm(ssm)
            .build());

    ssm.startScreen();
  }
}
