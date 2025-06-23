package net.anei.cadpage.parsers.KY;


import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class KYMetcalfeCountyAParser extends DispatchB2Parser {
  
  public KYMetcalfeCountyAParser() {
    super("911-CENTER:",CITY_LIST, "METCALFE COUNTY", "KY");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "ANDERSON PERKINS",
        "BILL POYNTER",
        "BOBBY MCCANDLESS",
        "BRANSTETTER PARK",
        "C HURT",
        "CAVE RIDGE NEW LIBERTY",
        "CEDAR TOP CHURCH",
        "CENTER CRAIL HOPE",
        "CENTER THREE SPRINGS",
        "CODY TRENT",
        "CRAIL HOPE",
        "CRENSHAW CASSIDY",
        "EDDIE WALKER",
        "FAIRVIEW PASCAL CHURCH",
        "FROEDGE DUBRE",
        "GOOD LUCK BEAUMONT",
        "GOODLUCK BEAUMONT",
        "HAROLD POYNTER",
        "HARVEY WHITE CEMETERY",
        "JACK SHAW",
        "KEN POYNTER",
        "KNOB LICK BLUE SPRINGS",
        "KNOB LICK WISDOM",
        "LIBERTY BIG MEADOW",
        "MCCANDLESS COOMER",
        "MOUNTY MARIAH",
        "P BOWELS",
        "RALPH EDWARDS",
        "RANDOLPH GOODLUCK",
        "RANDOLPH SCHOOL",
        "RANDOLPH SUMMER SHADE",
        "ROBERTSON SHAW",
        "ROCKLAND MILLS",
        "ROGERS CREEK",
        "ROY LEE HUMES",
        "SEVEN SPRINGS CHURCH",
        "SHADY GROVE WHICKERVILLE",
        ":SOUTH UNION HILL SCHOOL",
        "SULPHUR KNOB LICK",
        "SULPHUR WELL CENTER",
        "SULPHUR WELL KNOB LICK",
        "SULPHUR WELL- KNOB LICK",
        "SUMMER SHADE",
        "THURMAN SMITH",
        "TIMMY CIRCLE",
        "TOBY HILL",
        "WATER TOWER",
        "WILBUR GLASS"
    );
  }
  
  @Override
  public String getFilter() {
    return "911-CENTER@cityofedmontonky.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    data.strName = stripFieldStart(data.strName, "CITY OF");
    return true;
  }

  private static final String[] CITY_LIST = new String[]{
    "BEAUMONT",
    "CENTER",
    "EDMONTON",
    "KNOB LICK",
    "RANDOLPH",
    "SAVOYARD",
    "SULPHUR WELL",
    "SUMMER SHADE",
    "WISDOM",
    
    "BARREN CO",
    "BARREN COUNTY",
    "GLASGOW",
    
    "HART CO",
    "HART COUNTY",
    "HARDYVILLE",
    "HORSE CAVE",
    
    "MONROE CO",
    "MONROE COUNTY"
    
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ANIMAL COMPLT UNSPECIFED ANIMA",
      "ANY ALARM CALL",
      "ASSIST AMBULANCE SERVICE",
      "FIRE - HOUSE",
      "MEDICAL ASSIST",
      "MVA NON INJURY",
      "MVA WITH INJURIES",
      "SPECIAL DETAIL",
      "TEST CALL",
      "TREE DOWN",
      "UNDEFINED TYPE OF FIRE"
     
  );
}
