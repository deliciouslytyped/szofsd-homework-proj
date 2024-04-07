package game.ui;

import lombok.SneakyThrows;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.Objects;

@Slf4j
public class SceneStateMachine {

  private Stage stage;
  private UIState state;

  public SceneStateMachine(@NonNull Stage stage) {
    this.stage = stage;
  }

  public static Stage getActionStage(ActionEvent actionEvent) {
    var stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    return stage;
  }

  // TODO Masks I think IOException?
  @SneakyThrows
  private void loadScene(String name, String resourcePath) {
    // TODO: error codes using resource bundles?
    // TODO: any good way to DRY this?
    log.trace("{}", getClass()); //TODO used these for print debugging, but they werent helpful because the problem was that I used a typod filename
    log.trace("{}", getClass().getResource("/"));
    // TODO how to conditional the string format?
    var resource = Objects.requireNonNull(getClass().getResource(resourcePath), String.format("Failed to find resource `%s`.", resourcePath));

/*
    //TODO this makes us have to make assumtions, see TODOs on fixes in BaseController, there should be a less roundabout way to do it
    var loader = new FXMLLoader(resource);
    Parent root = loader.load(resource); // TODO isnt this redundant with the constructor parameter?
    var scene = new Scene(root);
    BaseController c = loader.getController();
    if (c == null) {
      log.trace("Controller for {}:{} is null", name, resourcePath);
      }
    if (root == null) {
      log.trace("root is null");
      }
    c.setRoot(root);
*/
    var scene = new Scene(FXMLLoader.load(resource));

    // TODO: for some reason example code uses stage.show() after each of these,
    // but it's not actually needed? Why do they do it?
    stage.setScene(scene);

    log.info("Loaded {} scene from {}.", name, resourcePath);
    stage.setUserData(state);
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
