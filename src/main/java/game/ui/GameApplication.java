package game.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

import game.ui.SceneStateMachine;

//import org.scenicview.ScenicView;

import game.Settings;

@Slf4j
public class GameApplication extends Application {

  private SceneStateMachine ssm;

  // TODO: Why is this marked NonNull in the examples?
  // Are there situations where this can be null? Does anything other than JavaFX call start?
  // TODO: Any specific reason for marking the stage argument final in examples?
  @Override
  public void start(@NonNull Stage stage) throws IOException {
    if(!Util3D.check3DSupport()){
      return;
    }

    ssm = new SceneStateMachine(stage);
    // TODO This is basically a global variable for stages,
    // but we have to do some hacks in StageController to get at it, so that needs to be refactored.
    stage.setUserData(UIState.builder()
            .ssm(ssm)
            .build());

    ssm.startScreen();

    if (System.getProperty(Settings.DebugFXSetting) != null){
      stage.setAlwaysOnTop(true);
      //ScenicView.show(stage.getScene());
    }


  }
}
