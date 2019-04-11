package frc.team852.lib.utils.fightstick;

/**
 * Simple, quick interface to ensure type similarities
 */

public interface FightStickButton {

  FightStickInput.input getButtonInputType();

  boolean get();

}
