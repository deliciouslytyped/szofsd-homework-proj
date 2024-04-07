package game.ui;

import lombok.extern.slf4j.Slf4j;
import lombok.SneakyThrows;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

@Slf4j
public class StartScreenController extends StageController {
  public void onPlayButtonAction(ActionEvent actionEvent) {
    log.trace("Play button clicked"); // TODO
    state.ssm.play();
  }

}
