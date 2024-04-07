package game;

import javafx.application.Application;
import game.ui.GameApplication;
import game.AppRelative;
import lombok.extern.slf4j.Slf4j;

// TODO: any specific reason this is final in examples?
@Slf4j
public class Main {
  private static String logSetting = "app.logPath";

  private static void setLogPath() {
    // Used to set the Log4j log file location next to the entrypoint.
    var loc = AppRelative.logDir().toString();
    // See log4j2.xml for how we make this work.
    System.setProperty(logSetting, loc);
    log.trace("{}: {}", logSetting, loc);
  }

  public static void main(String[] args) {
    // See the developer notes.
    System.setProperty("prism.forceGPU", "true");
    setLogPath();
    // The cast is needed to prevent the varargs from eating the rest of the array
    // https://stackoverflow.com/questions/39589879/printing-an-array-with-slf4j-only-prints-the-first-element
    log.trace("args: {}", (Object)args);
    log.trace("sysprops: {}", System.getProperties());
    Application.launch(GameApplication.class, args);
  }
}
