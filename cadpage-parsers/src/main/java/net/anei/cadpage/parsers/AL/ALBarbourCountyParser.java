package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchC12Parser;

public class ALBarbourCountyParser extends DispatchC12Parser {

  public ALBarbourCountyParser() {
    super(CITY_LIST, "BARBOUR COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strAddress = stripFieldEnd(data.strAddress, " BEGIN");
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CLIO",
      "EUFAULA",

      // Towns
      "BAKERHILL",
      "BLUE SPRINGS",
      "CLAYTON",
      "LOUISVILLE",

      //Unincorporated communities
      "BATESVILLE",
      "ELAMVILLE",
      "SPRING HILL",
      "TEALS CROSSROADS",

      // Bullock County
      "MIDWAY",

      // Dale County
      "ARITON",
      "CLOPTON",
      "SKIPPERVILLE",

      // Henry County
      "HENRY COUNTY",
      "ABBEVILLE",

      // Houston County
      "DOTHAN"


  };

}
