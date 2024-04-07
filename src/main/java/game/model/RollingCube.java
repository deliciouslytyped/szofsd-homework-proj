package game.model;

import lombok.extern.slf4j.Slf4j;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;

@Slf4j
public class RollingCube {
  private ReadOnlyObjectWrapper<Integer> activeFace = new ReadOnlyObjectWrapper();

  public RollingCube() {
    reset();
  }

  public void reset(){
    activeFace.setValue(CubeFace.TOP);
  }

  public ReadOnlyObjectProperty<Integer> activeFaceProperty() {
    return activeFace.getReadOnlyProperty();
  }

  public void roll(Direction d){
    var next = nextRedFace(d);
    log.trace("setting model active face from {} to {} through direction {}", activeFace.getValue(), next, d);
    activeFace.setValue(next);
  }

  //TODO separate CubeDirection
  public int nextRedFace(Direction d){
    return switch (d) {
      case UP -> rollForwards(activeFace.getValue());
      case RIGHT -> rollRight(activeFace.getValue());
      case DOWN -> rollBackwards(activeFace.getValue());
      case LEFT -> rollLeft(activeFace.getValue());
    };
  }

  public static Integer rollBackwards(int currentRedFace) {
    return switch (currentRedFace){
      case CubeFace.TOP -> CubeFace.BACK;
      case CubeFace.BACK -> CubeFace.BOTTOM;
      case CubeFace.BOTTOM -> CubeFace.FRONT;
      case CubeFace.LEFT -> CubeFace.LEFT;
      case CubeFace.RIGHT -> CubeFace.RIGHT;
      case CubeFace.FRONT -> CubeFace.TOP;
      default -> throw new IllegalStateException("Unexpected value: " + currentRedFace);
    };
  }

  public static Integer rollForwards(int currentRedFace) {
    return switch (currentRedFace){
      case CubeFace.BACK -> CubeFace.TOP; // note these are the reverse of the above
      case CubeFace.BOTTOM -> CubeFace.FRONT;
      case CubeFace.FRONT -> CubeFace.BOTTOM;
      case CubeFace.LEFT -> CubeFace.LEFT;
      case CubeFace.RIGHT -> CubeFace.RIGHT;
      case CubeFace.TOP -> CubeFace.FRONT;
      default -> throw new IllegalStateException("Unexpected value: " + currentRedFace);
    };
  }

  public static Integer rollRight(int currentRedFace) {
    return switch (currentRedFace){
      case CubeFace.TOP -> CubeFace.RIGHT;
      case CubeFace.RIGHT -> CubeFace.BOTTOM;
      case CubeFace.BOTTOM -> CubeFace.LEFT;
      case CubeFace.LEFT -> CubeFace.TOP;
      case CubeFace.BACK -> CubeFace.BACK;
      case CubeFace.FRONT -> CubeFace.FRONT;
      default -> throw new IllegalStateException("Unexpected value: " + currentRedFace);
    };
  }

  public static Integer rollLeft(int currentRedFace) {
    return switch (currentRedFace){
      case CubeFace.RIGHT -> CubeFace.TOP;
      case CubeFace.BOTTOM -> CubeFace.RIGHT;
      case CubeFace.LEFT -> CubeFace.BOTTOM;
      case CubeFace.TOP -> CubeFace.LEFT;
      case CubeFace.BACK -> CubeFace.BACK;
      case CubeFace.FRONT -> CubeFace.FRONT;
      default -> throw new IllegalStateException("Unexpected value: " + currentRedFace);
    };
  }


}
