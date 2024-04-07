package game.ui;

import javafx.scene.shape.*;
import javafx.scene.shape.Box;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

  class Marker extends Box {
    Marker(){
      super(30, 30, 30);
      setMaterial(new PhongMaterial(Color.BLUE));
    }
  }
