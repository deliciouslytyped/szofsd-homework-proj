//TODO this whole thing is a mess
package game.ui;

import lombok.extern.slf4j.Slf4j;
import lombok.SneakyThrows;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

import game.ui.SceneStateMachine;
import game.ui.UIState;

@Slf4j
public abstract class StageController {

  //TODO This sucks; this mess is for passing the user data around
  // https://stackoverflow.com/questions/13246211/how-to-get-stage-from-controller-during-initialization
  // TODO need to documenta lot of stuff related to this in devdocs
  @FXML
  private Pane stageHack;
  protected Stage stage;


  //TODO figure out what to name this class, it's semi-global state local to a given stage
  protected UIState state;

/*
  // TODO there should be a way to get this without doing it in such a roundabout way?
  // (we pass it in after using the FXML loader at the call site)
  protected Parent root;
*/

  //TODO in the tictactoe example is this overriding the fxml initialize or is it actually their own?
  //TODO is there a documentation bug on the init
  // https://openjfx.io/javadoc/20/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html#attributes
  @FXML
  protected void initialize(){
    // https://stackoverflow.com/questions/13246211/how-to-get-stage-from-controller-during-initialization/30910015#30910015
    stageHack.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
      if (oldScene == null && newScene != null) {
        log.trace("New scene initialized.");

        // Make a quit key availible in every scene
        newScene.setOnKeyPressed(k -> {
          log.trace("key pressed: {}", k);
          switch (k.getCode()) {
            case Q -> { System.exit(0); }
          };
        });

        newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
          if (oldWindow == null && newWindow != null) {
            log.trace("New window initialized.");
            //properties = (UIState) ((Stage)stageHack.getScene().getWindow()).getUserData();
            stage = (Stage)stageHack.getScene().getWindow();
            state = (UIState) stage.getUserData();

            myInitialize(); //TODO this needs a rename
            log.trace("New controller initialized.");
          }
        });
      }
    });
  }
/*
  // See comments on the attribute
  protected void setRoot(Parent root){
    this.root = root;
  }
*/
  // See initialize() listener
  protected void myInitialize(){
  }

}

