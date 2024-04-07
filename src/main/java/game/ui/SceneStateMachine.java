package game.ui;

import lombok.NonNull;

import java.util.Objects;
import javafx.stage.Stage;

public class SceneStateMachine extends BaseSceneStateMachine {

  public SceneStateMachine(@NonNull Stage stage) {
    super(stage);
  }

  public void startScreen() {
    stage.setTitle("JavaFX Game Demo");

    // TODO: rename xml without screen suffix?
    loadScene("StartScreen", "/fxml/startscreen.fxml");
    stage.show();
  }

  public void play() {
    // TODO: rename xml without screen suffix?
    loadScene("Game", "/fxml/gamescreen.fxml");
  }

}
