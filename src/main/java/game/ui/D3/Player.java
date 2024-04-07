package game.ui;

import javafx.scene.shape.*;
import javafx.scene.shape.Box;
import javafx.scene.Group;

import game.model.Position;

class Player extends Group {

  TexturedCube cube;

  Player(){
    super();
    //this.getChildren().add(edgeHandle)
    /*Box b = new Box(100, 100, 100);
    b.setMaterial(new PhongMaterial(Color.GREEN));*/
    cube = new TexturedCube(100, 1);
    getChildren().add(cube);

    getChildren().add(new Marker());
  }

  //TODO breaks encapsulation
  public void setPosition(Position newPos) {
    getParent().setTranslateX(newPos.row()*100); //TODO bind something instead?
    getParent().setTranslateZ(newPos.col()*100);
  }

  }
