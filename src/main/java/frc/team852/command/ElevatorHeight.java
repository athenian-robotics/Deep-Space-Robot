package frc.team852.command;

public enum ElevatorHeight {
  FEED_STATION(8), HATCH_LOW(20), HATCH_MID(90), HATCH_HIGH(170), CARGO_LOW(40), CARGO_SHIP(90), CARGO_MID(110), CARGO_HIGH(190);
  final int value;

  ElevatorHeight(int value) {
    this.value = value;
  }
}
