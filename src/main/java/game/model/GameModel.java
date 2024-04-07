//TODO new cube local and global direction object, this is too confusing
package game.model;

import lombok.extern.slf4j.Slf4j;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleObjectProperty;

@Slf4j
public class GameModel {
  // Needs to be initialized empty so we can trigger the map load event later after the attribute is bound by loading it.
  // TODO but maybe we should look into subclassing and cache firing until the first thing is bound or add something to collect events from firing until its told binding phase is finished?
  private ReadOnlyObjectWrapper<Map> map = new ReadOnlyObjectWrapper();
  private ReadOnlyBooleanWrapper goal = new ReadOnlyBooleanWrapper(false);

  // NOTE if we put the position on the player object instead of handling it on the map like this, player would have more freedom?
  private ReadOnlyObjectWrapper<Position> position = new ReadOnlyObjectWrapper();

  private ReadOnlyObjectWrapper<RollingCube> player = new ReadOnlyObjectWrapper(new RollingCube());

/*  public GameModel() {
  }*/

  public void setMap(){
    //TODO make a Map object for this
    map.setValue(new Map());//TODO map is hard-coded right now
  }

  public void initPlayerPosition(Position p){
    position.setValue(p);
  }

  public ReadOnlyObjectProperty<RollingCube> playerProperty() {
      return player.getReadOnlyProperty();
  }

  public ReadOnlyBooleanProperty goalProperty() {
      return goal.getReadOnlyProperty();
  }

  public ReadOnlyObjectProperty<Map> mapProperty() {
      return map.getReadOnlyProperty();
  }

  public ReadOnlyObjectProperty<Position> positionProperty() {
      return position.getReadOnlyProperty();
  }

  //TODO not sure if there is a direct way to fire property change events
  public void debugFireMap(){
    log.trace("reset map");
    setMap(); // TODO this doesnt work (which makes sense) if @Data is set on map
  }

  public boolean spaceFreeFromTo(Position p, Direction d){
    var newPos = p.getPosition(d);
    var v = map.getValue().get(newPos.row(), newPos.col());
    var r = v == 0;
    log.trace("space from {} (to {}) in direction {} is free?: {} == 0 is {}", p, newPos, d, v, r);
    return r;
  }

  public boolean canMove(Direction d){
    var nextRed = player.getValue().nextRedFace(globalToCubeLocal(d));
    var b = spaceFreeFromTo(position.getValue(), d) && (nextRed != CubeFace.BOTTOM);
    log.trace("can move in direction {} with current red face {} and next red face {}?: {}", d, player.getValue().activeFaceProperty().getValue(), nextRed, b);
    return b;
  }


  public Direction globalToCubeLocal(Direction d){
    return switch(d){
      case UP -> Direction.RIGHT;
      case DOWN -> Direction.LEFT;
      case LEFT -> Direction.UP;
      case RIGHT -> Direction.DOWN;
    };
  }

  public void tryMove(Direction d){
    log.trace("trying to move: {}", d);
    if (canMove(d)) {
      position.setValue(position.getValue().getPosition(d));
      log.trace("model setting rollingcube active face to {} through direction {}", player.getValue().nextRedFace(globalToCubeLocal(d)), d);
      player.getValue().roll(globalToCubeLocal(d));
    }
  }

}
