package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class PAVenangoCountyAParser extends DispatchB2Parser {

  public PAVenangoCountyAParser() {
    super("VENANGO911:", CITY_LIST, "VENANGO COUNTY", "PA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "EAGLE ROCK",
        "PETROLEUM CENTER",
        "RUSSELL CORNERS",
        "VO TECH",
        "WHITE CITY"
   );
  }
  
  @Override
  public String getFilter() {
    return "777,VENANGO911@co.venango.pa.us";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("VENANGO 911:")) body = "VENANGO911:" + body.substring(12);
    return super.parseMsg(body, data);
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "EMS LIFELINE CALL",
      "FIRE SMOKE INVESTIGATION",
      "FIRE TREE DOWN",
      "MEDICAL ALPHA",
      "MEDICAL BRAVO",
      "MEDICAL CHARLIE",
      "MEDICAL DELTA",
      "MEDICAL GENERIC"
  );

  private static final String[] CITY_LIST = new String[]{
    "FRANKLIN",
    "OIL CITY",

    "BARKEYVILLE",
    "CLINTONVILLE",
    "COOPERSTOWN",
    "EMLENTON",
    "PLEASANTVILLE",
    "POLK",
    "ROUSEVILLE",
    "SUGARCREEK",
    "UTICA",

    "ALLEGHENY",
    "CANAL",
    "CHERRYTREE",
    "CLINTON",
    "CORNPLANTER",
    "CRANBERRY",
    "FRENCHCREEK",
    "IRWIN",
    "JACKSON",
    "MINERAL",
    "OAKLAND",
    "OIL CREEK TOWNSHIP",
    "PINEGROVE",
    "PLUM",
    "PRESIDENT",
    "RICHLAND",
    "ROCKLAND",
    "SANDYCREEK",
    "SCRUBGRASS",
    "VICTORY"
  };
}
