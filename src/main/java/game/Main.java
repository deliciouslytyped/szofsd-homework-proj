package game;

import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import game.ui.GameApplication;

// TODO: any specific reason this is final in examples?
@Slf4j
public class Main {
  public static void main(String[] args) {
    // See the developer notes.
    System.setProperty("prism.forceGPU", "true");
    // The cast is needed to prevent the varargs from eating the rest of the array
    // https://stackoverflow.com/questions/39589879/printing-an-array-with-slf4j-only-prints-the-first-element
    log.trace("args: {}", (Object)args);
    log.trace("sysprops: {}", System.getProperties());
    Application.launch(GameApplication.class, args);
  }
}
