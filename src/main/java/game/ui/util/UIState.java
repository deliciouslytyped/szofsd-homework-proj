package game.ui;

import lombok.*;

import game.ui.SceneStateMachine;
import game.ui.UserProperties;

@Builder
public class UIState {
  UserProperties p;
  SceneStateMachine ssm;
  }
