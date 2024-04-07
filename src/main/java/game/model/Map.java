package game.model;

import lombok.*;

//TODO
//@Data
public class Map {
  public int[][] data = {
            {0, 1, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 1, 0, 1, 0},
            {0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0},
            {0, 0, 1, 0, 1, 0, 0},
            {0, 1, 0, 0, 0, 0, 0},
            };

  public int width(){
    return data.length;
  }

  public int get(int i, int j){
    return data[i][j];
  }
}
