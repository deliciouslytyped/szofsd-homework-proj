package game.model;

public enum CubeFace {
  BOTTOM(3),
  TOP(1),
  LEFT(4),
  RIGHT(5),
  FRONT(6),
  BACK(2);

  int face;
  CubeFace(int face) {
    this.face = face;
  };
};
