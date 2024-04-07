//TODO the following is not really valid anymore.
// I now do similar to https://github.com/wangdapang77/Java8-Source-Code/blob/master/src/main/jdk8/java/net/JarURLConnection.java
// and https://stackoverflow.com/questions/402683/how-do-i-get-just-the-jar-url-from-a-jar-url-containing-a-and-a-specific-fi/9189036#9189036
// // TODO do the values this works with work on windows?
// // I explicitly avoid naive string manipulation, so they should.

// TODO this is currently a bit wrong, because it used to be part of Main.
// Since it's factored out, it's not strictly a path to the entry point,
// but Main and AppRelative are in the same package so it should be fine.

package game;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.file.Path;

// This class is final because: see the noted trick in entrypointLocation()
@Slf4j
public final class AppRelative {
  // Partially based off of https://www.baeldung.com/java-full-path-of-jar-from-class
  // TODO the supressed URISyntaxException should be impossible?
  @SneakyThrows
  public static URI entrypointLocation() {
    // Trick from https://stackoverflow.com/questions/8275499/how-to-call-getclass-from-a-static-method-in-java/17397548#17397548
    Class clazz = new Object() { }.getClass().getEnclosingClass();
    var logDir = clazz.getResource(clazz.getSimpleName() + ".class").toURI();
    return logDir;
  }

  @SneakyThrows
  public static Path logDir() {
    // TODO this is not exactly reliable, there are a lot of variations
    // where this might not make sense, but as it is, it works for us.
    // NOTE this uses the path of the class or the JAR depending on what is being run.
    var path = entrypointLocation();
    // TODO Technically entrypointLocation can be anywhere,
    // but we assume it's a local path we can write to, so assert that?
    // TODO NOTE JDK has two kinds of filesystems per the following, so this should be exhaustive.
    // https://stackoverflow.com/questions/32873899/converting-a-jar-uri-into-a-nio-path/32874771#32874771
    // TODO this meanwhile talks about URI spec violation https://stackoverflow.com/questions/9530549/why-contextclassloader-returns-path-with-exclamation-character/9530744#9530744
    if(path.getScheme().equals("jar")) {
      path = new URI("file", new URI(path.getSchemeSpecificPart()).getPath().split("!/")[0],null);
      System.out.println(path.toString());
    }
    var logDir = Path.of(path).getParent();
    //TODO As usual, I'm an idiot. This is called too early and this is probably what leads to the log4j ordering mess.
    //log.trace("entrypointLocation: {}", path.toString());
    return logDir;
  }
}

