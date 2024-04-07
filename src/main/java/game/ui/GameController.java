package game.ui;

import lombok.extern.slf4j.Slf4j;
import lombok.SneakyThrows;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class GameController implements Initializable {

  //TODO is there a documentation bug on the ini
  // https://openjfx.io/javadoc/20/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html#attributes
  @FXML
  @Override
  public void initialize(URL location, ResourceBundle resources){
    log.trace("New controller initialized.");
  }

}
