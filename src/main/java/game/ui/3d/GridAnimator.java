package game.ui;

//TODO clean this up, it breaks encapsulation of the scene grap on player and other things probably

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

import java.util.List;
import java.util.ArrayList;

// TODO this is currently tightly coupled
public class GridAnimator implements IGridAnimator {
    TexturedCube n;
    Group parent;
    boolean inProgress;
    int millis = 200;
    List<Integer> rotations = new ArrayList<Integer>();
    GridAnimator(Player n) {
      this.n = n.cube;
      this.parent = (Group)n.getParent();
      parent.getChildren().add(new Marker());
    }

    Timeline tweener(DoubleProperty prop, double offset){
      Timeline t = new Timeline(
              new KeyFrame(Duration.ZERO, new KeyValue(prop, prop.getValue())),
              new KeyFrame(Duration.millis(millis), new KeyValue(prop, prop.getValue() + offset))
      );
      return t;
    }

    void tryAnim(Rotate r, boolean negative, Runnable callback){
      if (inProgress) return;
      else inProgress = true;
      var prop = r.angleProperty();
      var ts = n.getTransforms();
      ts.add(r); // TODO accumulate instead of garbage
      var tw = tweener(prop, negative ? -90 : 90);
      tw.setOnFinished(event -> {
        n.getTransforms().clear();
        n.setRotationAxis(Rotate.X_AXIS);
        n.setRotate(0);
        n.setRotationAxis(Rotate.Y_AXIS);
        n.setRotate(0);
        n.setRotationAxis(Rotate.Z_AXIS);
        n.setRotate(0);
        callback.run();
        inProgress = false;
      });
      tw.play();

    }
    @Override
    public void animateUp() {
      var p = n.parentToLocal(0, 50, 50);
      Rotate r = new Rotate(0, p.getX(), p.getY(), p.getZ(), Rotate.X_AXIS);
      tryAnim(r, true, () -> {
        parent.setTranslateZ(parent.getTranslateZ() + 100);
        var newside = switch (n.activeSide){
          case 1 -> 2;
          case 2 -> 3;
          case 3 -> 6;
          case 4 -> 4;
          case 5 -> 5;
          case 6 -> 1;
          default -> throw new IllegalStateException("Unexpected value: " + n.activeSide);
        };
        n.setSide(newside); // need to do it so everything is always relatiev to side on top, facing front, orientation
      });
    }

    @Override
    public void animateDown() {
      var p = n.parentToLocal(0, 50, -50);
      Rotate r = new Rotate(0, p.getX(), p.getY(), p.getZ(), Rotate.X_AXIS);
      tryAnim(r, false, () ->{
        parent.setTranslateZ(parent.getTranslateZ() - 100);
        n.getTransforms().clear();
        var newside = switch (n.activeSide){
          case 2 -> 1; // note these are the reverse of the above
          case 3 -> 2;
          case 6 -> 3;
          case 4 -> 4;
          case 5 -> 5;
          case 1 -> 6;
          default -> throw new IllegalStateException("Unexpected value: " + n.activeSide);
        };
        n.setSide(newside); // need to do it so everything is always relatiev to side on top, facing front, orientation
      });
    }

    @Override
    public void animateLeft() {
      var p = n.parentToLocal(-50, 50, 0);
      Rotate r = new Rotate(0, p.getX(), p.getY(), p.getZ(), Rotate.Z_AXIS);
      tryAnim(r, true, () -> {
        parent.setTranslateX(parent.getTranslateX() - 100);
        n.getTransforms().clear();
        var newside = switch (n.activeSide){
          case 1 -> 5;
          case 5 -> 3;
          case 3 -> 4;
          case 4 -> 1;
          case 2 -> 2;
          case 6 -> 6;
          default -> throw new IllegalStateException("Unexpected value: " + n.activeSide);
        };
        n.setSide(newside); // need to do it so everything is always relatiev to side on top, facing front, orientation
      });
    }

    @Override
    public void animateRight() {
      var p = n.parentToLocal(50, 50, 0);
      Rotate r = new Rotate(0, p.getX(), p.getY(), p.getZ(), Rotate.Z_AXIS);
      tryAnim(r, false, () -> {
        parent.setTranslateX(parent.getTranslateX() + 100);
        n.getTransforms().clear();
        var newside = switch (n.activeSide){
          case 5 -> 1;
          case 3 -> 5;
          case 4 -> 3;
          case 1 -> 4;
          case 2 -> 2;
          case 6 -> 6;
          default -> throw new IllegalStateException("Unexpected value: " + n.activeSide);
        };
        n.setSide(newside); // need to do it so everything is always relatiev to side on top, facing front, orientation
      });

    }
  }
