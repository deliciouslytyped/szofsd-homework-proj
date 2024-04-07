package game.model;

import lombok.extern.slf4j.Slf4j;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;

@Slf4j
public class RollingCube {
  private ReadOnlyObjectWrapper<CubeFace> activeFace = new ReadOnlyObjectWrapper();

  public RollingCube() {
    reset();
  }

  public void reset(){
    activeFace.setValue(CubeFace.TOP);
  }

  public ReadOnlyObjectProperty<CubeFace> activeFaceProperty() {
    return activeFace.getReadOnlyProperty();
  }

  public void roll(CubeDirection d){
    var next = nextRedFace(d);
    log.trace("setting model active face from {} to {} through direction {}", activeFace.getValue(), next, d);
    activeFace.setValue(next);
  }

  //TODO separate CubeDirection
  public CubeFace nextRedFace(CubeDirection d){
    return switch (d) {
      case FORWARD -> rollForwards(activeFace.getValue());
      case RIGHT -> rollRight(activeFace.getValue());
      case BACKWARD -> rollBackwards(activeFace.getValue());
      case LEFT -> rollLeft(activeFace.getValue());
    };
  }

  public static CubeFace rollBackwards(CubeFace currentRedFace) {
    return switch (currentRedFace){
      case TOP -> CubeFace.BACK;
      case BACK -> CubeFace.BOTTOM;
      case BOTTOM -> CubeFace.FRONT;
      case LEFT -> CubeFace.LEFT;
      case RIGHT -> CubeFace.RIGHT;
      case FRONT -> CubeFace.TOP;
    };
  }

  public static CubeFace rollForwards(CubeFace currentRedFace) {
    return switch (currentRedFace){
      case BACK -> CubeFace.TOP; // note these are the reverse of the above
      case BOTTOM -> CubeFace.FRONT;
      case FRONT -> CubeFace.BOTTOM;
      case LEFT -> CubeFace.LEFT;
      case RIGHT -> CubeFace.RIGHT;
      case TOP -> CubeFace.FRONT;
    };
  }

  public static CubeFace rollRight(CubeFace currentRedFace) {
    return switch (currentRedFace){
      case TOP -> CubeFace.RIGHT;
      case RIGHT -> CubeFace.BOTTOM;
      case BOTTOM -> CubeFace.LEFT;
      case LEFT -> CubeFace.TOP;
      case BACK -> CubeFace.BACK;
      case FRONT -> CubeFace.FRONT;
    };
  }

  public static CubeFace rollLeft(CubeFace currentRedFace) {
    return switch (currentRedFace){
      case RIGHT -> CubeFace.TOP;
      case BOTTOM -> CubeFace.RIGHT;
      case LEFT -> CubeFace.BOTTOM;
      case TOP -> CubeFace.LEFT;
      case BACK -> CubeFace.BACK;
      case FRONT -> CubeFace.FRONT;
    };
  }


}
