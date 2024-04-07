// https://stackoverflow.com/questions/49130485/javafx-shape3d-texturing-dont-strectch-the-image
package game.ui;

import lombok.extern.slf4j.Slf4j;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import game.model.CubeFace;

@Slf4j
public class TexturedCube extends MeshView {
  float[] points;
  float[] tex;
  float[] normals;
  int[] faces;

  public ObjectProperty<Integer> activeSide;
  TexturedCube(float size, int side){
  super();

  genData(size, size, size);

  TriangleMesh mesh = new TriangleMesh();
  mesh.setVertexFormat(VertexFormat.POINT_NORMAL_TEXCOORD);
  mesh.getPoints().addAll(points);
  mesh.getTexCoords().addAll(tex);
  mesh.getNormals().addAll(normals);
  mesh.getFaces().addAll(faces);

  setMesh(mesh);
  activeSide = new SimpleObjectProperty<Integer>();
  activeSide.addListener((observable, oldVal, newVal) -> {
    setTexture(newVal);
  });
  activeSide.setValue(CubeFace.TOP);
  }

  private void setTexture(int side){
    log.trace("setting texture active side to {}", side);
    var ph = new PhongMaterial();
    Canvas canvas = new Canvas(4*32,3*32); // the interpolation leads to soft edges. not sure how to turn it off.
    var gc = canvas.getGraphicsContext2D();
    //gc.setImageSmoothing(false); // probably doesnt apply / doesnt do what i want
    gc.setFill(Color.BLUE);
    gc.fillRect(0,0,16*32,12*32);
    gc.setFill(Color.RED);
    switch (side){
      case CubeFace.TOP -> { gc.fillRect(1*32,0*32,1*32,1*32); } // "top"
      case CubeFace.FRONT -> { gc.fillRect(1*32,1*32,1*32,1*32); } // "front"
      case CubeFace.BOTTOM -> { gc.fillRect(1*32,2*32,1*32,1*32); } // "bottom"
      case CubeFace.RIGHT -> { gc.fillRect(0*32,1*32,1*32,1*32); } // right
      case CubeFace.LEFT -> { gc.fillRect(2*32,1*32,1*32,1*32); } // left
      case CubeFace.BACK -> { gc.fillRect(3*32,1*32,1*32,1*32); } // back
    }
    if (side == 3){
      log.error("bottom active.");
    }
    ph.setDiffuseMap(canvas.snapshot(null,null));
    setMaterial(ph);
  }
  void genData(float w, float h, float d){
    float hw = w / 2f;
    float hh = h / 2f;
    float hd = d / 2f;

    points = new float[] {
            hw,  hh,  hd,
            hw,  hh, -hd,
            hw, -hh,  hd,
            hw, -hh, -hd,
            -hw,  hh,  hd,
            -hw,  hh, -hd,
            -hw, -hh,  hd,
            -hw, -hh, -hd};

    float L = 2 * w + 2 * d;
    float H = h + 2 * d;

    tex = new float[] {
            d / L,          0f,
            (d + w) / L,          0f,
            0f,       d / H,
            d / L,       d / H,
            (d + w) / L,       d / H,
            (2 * d + w) / L,       d / H,
            1f,       d / H,
            0f, (d + h) / H,
            d / L, (d + h) / H,
            (d + w) / L, (d + h) / H,
            (2 *d + w) / L, (d + h) / H,
            1f, (d + h) / H,
            d / L,          1f,
            (d + w) / L,          1f};

    normals = new float[] {
            1f,  0f,  0f,
            -1f,  0f,  0f,
            0f,  1f,  0f,
            0f, -1f,  0f,
            0f,  0f,  1f,
            0f,  0f, -1f,
    };

    faces = new int[] {
            0, 0, 10, 2, 0,  5, 1, 0,  9,
            2, 0,  5, 3, 0,  4, 1, 0,  9,
            4, 1,  7, 5, 1,  8, 6, 1,  2,
            6, 1,  2, 5, 1,  8, 7, 1,  3,
            0, 2, 13, 1, 2,  9, 4, 2, 12,
            4, 2, 12, 1, 2,  9, 5, 2,  8,
            2, 3,  1, 6, 3,  0, 3, 3,  4,
            3, 3,  4, 6, 3,  0, 7, 3,  3,
            0, 4, 10, 4, 4, 11, 2, 4,  5,
            2, 4,  5, 4, 4, 11, 6, 4,  6,
            1, 5,  9, 3, 5,  4, 5, 5,  8,
            5, 5,  8, 3, 5,  4, 7, 5,  3};
  }
}
