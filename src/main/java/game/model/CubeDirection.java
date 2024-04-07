//This type is used to differentiate local cube directions from global map directions
package game.model;

public enum CubeDirection {

  FORWARD,
  BACKWARD,
  LEFT,
  RIGHT;

  public static CubeDirection fromGlobal(Direction d) {
    return switch(d){
      case UP -> RIGHT;
      case DOWN -> LEFT;
      case LEFT -> FORWARD;
      case RIGHT -> BACKWARD;
    };
  }

}
