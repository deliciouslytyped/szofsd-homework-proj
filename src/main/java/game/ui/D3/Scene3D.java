package game.ui;

//TODO remove unneeded
import com.sun.prism.Texture;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.w3c.dom.Text;

import lombok.AllArgsConstructor;

public class Scene3D {
  @AllArgsConstructor
  public static class SceneSet {
    public Camera camera;
    public Group root;
  }

  @AllArgsConstructor
  public static class MapSet {
    public Player player;
    public Group root;
  }

  public static SceneSet prepareScene(SubScene scene, boolean perspectiveCam) {
    Group root = new Group();

    Camera camera;
    if (perspectiveCam) {
      camera = Util3D.mkPerspectiveCam();
    } else {
      camera = Util3D.mkIsoCam();
    }

    scene.setCamera(camera);
    scene.setFill(Color.SILVER);

    var light = new PointLight();
    light.setConstantAttenuation(0.8);
    light.setTranslateX(-1000); // Flat shading can be emulated by putting the point light far away
    light.setTranslateY(-100);
    light.setTranslateZ(-4000);
    root.getChildren().add(light);

    scene.setRoot(root); //TODO think through what needs to be done with this where, I forgot about it and then thins got a bit messed up
    return new SceneSet(camera, root);
  }

  //TODO factor and bind this to the model
  public static MapSet prepareMap(int[][] map){
    Group world2 = new Group();
    Group world = new Group();
    world2.getChildren().add(world);
    Group obstacles = new Group();

    for(int i = 0; i < map.length; i++){
      for(int j = 0; j < map.length; j++) {
        if (map[i][j] == 1) {
          Box b = new Box(100, 100, 100);
          var p = new PhongMaterial(Color.GRAY);
          b.setMaterial(p);
          b.setTranslateX(100 * i);
          b.setTranslateZ(100 * j);
          obstacles.getChildren().add(b);
        }
      }
    }
    //obstacles.setRotationAxis(Rotate.Y_AXIS);
    //obstacles.setRotate(90);
    /*for(int i = 1; i <= 6; i++){
      TexturedCube b = new TexturedCube(100, i);
      b.setTranslateX(500 + i * 200);
      obstacles.getChildren().add(b);
    }*/

    int startx = 1;
    int starty = 5;

    Player player = new Player();
    var outerPlayer = new Group(player);
    outerPlayer.setTranslateX(startx*100);
    outerPlayer.setTranslateZ(starty*100);
    obstacles.getChildren().add(outerPlayer);

    obstacles.setTranslateX(100/2);
    obstacles.setTranslateZ(100/2);
    obstacles.setTranslateY(-100/2);

    Box ground = new Box(100*map.length, 10,100*map.length);
    ground.setTranslateX(ground.getWidth()/2);
    ground.setTranslateZ(ground.getDepth()/2);
    ground.setTranslateY(ground.getHeight()/2);

    world.getChildren().add(obstacles);
    world.getChildren().add(ground);

    world.setTranslateX(-ground.getWidth()/2);
    world.setTranslateZ(-ground.getDepth()/2);
    world.setTranslateY(100);
    world.setRotationAxis(Rotate.Y_AXIS);
    world.setRotate(90);

    Rotate a = new Rotate(45, Rotate.X_AXIS);
    Rotate b = new Rotate(45, Rotate.Y_AXIS);
    world2.getTransforms().addAll(a, b);
    world2.getChildren().add(new Marker());
    return new MapSet(player, world2);
  }

}
