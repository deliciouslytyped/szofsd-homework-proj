package game;

import javafx.application.Application;
import game.ui.GameApplication;
import game.AppRelative;
import lombok.extern.slf4j.Slf4j;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.file.Path;

// TODO: any specific reason this is final in examples?
@Slf4j
public class Main {
  // Partially based off of https://www.baeldung.com/java-full-path-of-jar-from-class
  // This is used by the AppRelative class.
  // TODO the supressed URISyntaxException should be impossible?
  @SneakyThrows
  public static URI entrypointLocation() {
    // Trick from https://stackoverflow.com/questions/8275499/how-to-call-getclass-from-a-static-method-in-java/17397548#17397548
    Class clazz = new Object() { }.getClass().getEnclosingClass();
    var logDir = clazz.getResource(clazz.getSimpleName() + ".class").toURI();
    return logDir;
  }

  private static void setLogPath() {
    // Used to set the Log4j log file location next to the entrypoint.
    var loc = AppRelative.logDir().toString();
    // See log4j2.xml for how we make this work.
    System.setProperty(Settings.LogSetting, loc);
    log.trace("{}: {}", Settings.LogSetting, loc);
  }

  private static void forceGPU(){
    // See the developer notes.
    System.setProperty("prism.forceGPU", "true");
  }

  public static void main(String[] args) {
    forceGPU();
    setLogPath();

    // The cast is needed to prevent the varargs from eating the rest of the array
    // https://stackoverflow.com/questions/39589879/printing-an-array-with-slf4j-only-prints-the-first-element
    log.trace("args: {}", (Object)args);
    log.trace("sysprops: {}", System.getProperties());

    Application.launch(GameApplication.class, args);
  }
}
