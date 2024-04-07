package game.ui;

import java.lang.Runnable;

public interface IGridAnimator {
  void animateUp(Runnable finished);
  void animateDown(Runnable finished);
  void animateLeft(Runnable finished);
  void animateRight(Runnable finished);
}
