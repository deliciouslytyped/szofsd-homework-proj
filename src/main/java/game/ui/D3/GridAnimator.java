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
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ArrayList;
import java.lang.Runnable;
import java.util.function.Consumer;

import game.model.RollingCube;
import game.model.Position;

// TODO this is currently tightly coupled
@Slf4j
public class GridAnimator implements IGridAnimator {
    TexturedCube n;
    Group parent;
    boolean inProgress;
    int millis = 200;
    List<Integer> rotations = new ArrayList<Integer>();
    GridAnimator(Player n) {
      this.n = n.cube;
      this.parent = (Group)n.getParent();
      parent.getChildren().add(new Marker(Color.PURPLE));
    }

    Timeline tweener(DoubleProperty prop, double offset){
      Timeline t = new Timeline(
              new KeyFrame(Duration.ZERO, new KeyValue(prop, prop.getValue())),
              new KeyFrame(Duration.millis(millis), new KeyValue(prop, prop.getValue() + offset))
      );
      return t;
    }

    void tryAnim(Rotate r, boolean negative, Consumer<Position> callback, Position data){
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
        callback.accept(data);
        inProgress = false;
      });
      tw.play();

    }

//    @Override
    public void animateRight(Consumer<Position> finished, Position newPos) {
      log.trace("animateRight");
      var p = n.parentToLocal(0, 50, 50);
      Rotate r = new Rotate(0, p.getX(), p.getY(), p.getZ(), Rotate.X_AXIS);
      tryAnim(r, true, (_newPos) -> {
        parent.setTranslateZ(parent.getTranslateZ() + 100);
        finished.accept(_newPos);
      }, newPos);
    }

//    @Override
    public void animateLeft(Consumer<Position> finished, Position newPos) {
      log.trace("animateLeft");
      var p = n.parentToLocal(0, 50, -50);
      Rotate r = new Rotate(0, p.getX(), p.getY(), p.getZ(), Rotate.X_AXIS);
      tryAnim(r, false, (_newPos) ->{
        parent.setTranslateZ(parent.getTranslateZ() - 100);
        n.getTransforms().clear();
        finished.accept(_newPos);
      }, newPos);
    }

//    @Override
    public void animateUp(Consumer<Position> finished, Position newPos) {
      log.trace("animateUp");
      var p = n.parentToLocal(-50, 50, 0);
      Rotate r = new Rotate(0, p.getX(), p.getY(), p.getZ(), Rotate.Z_AXIS);
      tryAnim(r, true, (_newPos) -> {
        parent.setTranslateX(parent.getTranslateX() - 100);
        n.getTransforms().clear();
        finished.accept(_newPos);
      }, newPos);
    }

//    @Override
    public void animateDown(Consumer<Position> finished, Position newPos) {
      log.trace("animateDown");
      var p = n.parentToLocal(50, 50, 0);
      Rotate r = new Rotate(0, p.getX(), p.getY(), p.getZ(), Rotate.Z_AXIS);
      tryAnim(r, false, (_newPos) -> {
        parent.setTranslateX(parent.getTranslateX() + 100);
        n.getTransforms().clear();
        finished.accept(_newPos);
      }, newPos);

    }
  }
