package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDCarrollCountyCParser extends FieldProgramParser {
  
  public MDCarrollCountyCParser() {
    super(CITY_LIST, "CARROLL COUNTY", "MD", 
          "CALL PLACE? ADDR/SZ! Map:MAP! NARRATIVE:INFO! INFO/N+ FIREBOXINFO:BOX% END");
  }
  
  @Override
  public String getFilter() {
    return "85364";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldEnd(body, "...");
    return parseFields(body.split("\n"), data);
  }
  
  private static final String[] CITY_LIST = new String[]{

      // Cities
      "WESTMINSTER",
      "MOUNT AIRY",

      // Towns
      "MANCHESTER",
      "NEW WINDSOR",
      "UNION BRIDGE",
      "HAMPSTEAD",
      "SYKESVILLE",
      "TANEYTOWN",

      // Census-designated place
      "ELDERSBURG",

      // Unincorporated communities
      "ALESIA",
      "CARROLLTON",
      "CARROLLTOWNE",
      "DETOUR",
      "FINKSBURG",
      "FRIZZELBURG",
      "GAMBER",
      "GAITHER",
      "GREENMOUNT",
      "HARNEY",
      "HENRYTON",
      "JASONTOWN",
      "KEYMAR",
      "LINEBORO",
      "LINWOOD",
      "LOUISVILLE",
      "MARRIOTTSVILLE",
      "MAYBERRY",
      "MIDDLEBURG",
      "MILLERS",
      "PATAPSCO",
      "PLEASANT VALLEY",
      "SILVER RUN",
      "UNION MILLS",
      "UNIONTOWN",
      "WOODBINE",
      "WOODSTOCK",
      "YOUNG MANS FANCY"
  };

}
