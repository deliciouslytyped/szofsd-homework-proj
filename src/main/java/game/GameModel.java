package game;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;

public class GameModel {
  public ReadOnlyObjectWrapper<int[][]> map = new ReadOnlyObjectWrapper();

  public ReadOnlyBooleanWrapper goal = new ReadOnlyBooleanWrapper();
  public ReadOnlyObjectWrapper<int[]> position;

/*  public GameModel() {
  }*/

  public void setMap(){
    //TODO make a Map object for this
    map.setValue(new int[][] {
            {0, 1, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 1, 0, 1, 0},
            {0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0},
            {0, 0, 1, 0, 1, 0, 0},
            {0, 1, 0, 0, 0, 0, 0},
            });
  }

  public ReadOnlyBooleanProperty goalProperty() {
      return goal.getReadOnlyProperty();
  }

  public ReadOnlyObjectProperty<int[][]> mapProperty() {
      return map.getReadOnlyProperty();
  }

  //TODO not sure if there is a direct way to fire property change events
  public void debugFireMap(){
    setMap();
  }

}
