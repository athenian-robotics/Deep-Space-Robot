package frc.team852.lib.utils.fightstick;

public class FightStickInput {
  public enum input {
    POVtopLeft,
    POVtop,
    POVtopRight,
    POVleft,
    POVcenter,
    POVright,
    POVbotLeft,
    POVbot,
    POVbotRight,
    lightPunch,
    medPunch,
    heavyPunch,
    lightKick,
    medKick,
    heavyKick,
    share,
    option,
    L3,
    R3,
    R2,
    R1,
  }

  public static input getJoystickEnumValue(int povAngle) {

    input dir;

    switch (povAngle) {
      default:
      case -1:
        dir = input.POVcenter;
        break;
      case 0:
        dir = input.POVtop;
        break;
      case 45:
        dir = input.POVtopRight;
        break;
      case 90:
        dir = input.POVright;
        break;
      case 135:
        dir = input.POVbotRight;
        break;
      case 180:
        dir = input.POVbot;
        break;
      case 225:
        dir = input.POVbotLeft;
        break;
      case 270:
        dir = input.POVleft;
        break;
      case 315:
        dir = input.POVtopLeft;
        break;
    }

    return dir;
  }

}
