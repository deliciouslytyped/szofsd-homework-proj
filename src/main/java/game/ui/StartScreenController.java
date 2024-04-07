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
public class StartScreenController {

  //TODO: factor this
  @SneakyThrows
  private void loadScene(Stage stage, String resourcePath) {
    log.trace("Play button clicked, switching to {} scene.", resourcePath); // TODO
    // TODO: factor null check
    // TODO: rename xml without suffix?
    log.trace("{}", getClass()); //TODO used these for print debugging, but they werent helpful because the problem was that I used a typod filename
    log.trace("{}", getClass().getResource("/"));
    stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resourcePath)))));
  }

  public void onPlayButtonAction(ActionEvent actionEvent) {
    // TODO: factor scene changing?
    // TODO: for some reason example code uses stage.show() after each of these,
    // but it's not actually needed? Why do they do it?
    var stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    loadScene(stage, "/fxml/gamescreen.fxml");
  }

}
