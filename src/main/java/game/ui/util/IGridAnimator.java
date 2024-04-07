package game.ui;

import java.util.function.Consumer;

import game.model.Position;

//TODO generics result in foot shooting, not enough time to figure it out
public interface IGridAnimator {
  void animateUp(Consumer<Position> finished, Position d);
  void animateDown(Consumer<Position> finished, Position d);
  void animateLeft(Consumer<Position> finished, Position d);
  void animateRight(Consumer<Position> finished, Position d);
}
