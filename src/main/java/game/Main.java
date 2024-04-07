package game;

import javafx.application.Application;

public class Main {
  public static void main(String[] args) {
    // See the developer notes.
    System.setProperty("prism.forceGPU", "true");
    Application.launch(GameApplication.class, args);
  }
}
