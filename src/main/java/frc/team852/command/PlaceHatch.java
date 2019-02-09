package frc.team852.command;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PlaceHatch extends CommandGroup {
  private double[] heights = {50, 100, 150}; // Not sure what these values are TODO change values on reception of robot

  public PlaceHatch(int level) {
    addSequential(new ElevatorHold(heights[level]));
    addSequential(new FollowFloorTape());
    addSequential(new OutputHatch());
  }
}
