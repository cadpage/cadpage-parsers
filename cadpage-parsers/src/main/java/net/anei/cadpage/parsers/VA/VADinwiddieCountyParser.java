package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;



public class VADinwiddieCountyParser extends DispatchA48Parser {
  
  public VADinwiddieCountyParser() {
    super(CITY_LIST, "DINWIDDIE COUNTY", "VA", FieldType.PLACE, A48_NO_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern TIME_DOT_PTN = Pattern.compile(" (\\d\\d)\\.(:\\d\\d:\\d\\d) ");
    
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = TIME_DOT_PTN.matcher(body).replaceFirst(" $1$2 ");
    return super.parseMsg(subject, body, data);
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "AB PAIN",
      "ASSAULT RAPE RES",
      "BLEED NON TRAUMA",
      "BREATHING DIFF",
      "BRUSH FIRE",
      "CARD RESP ARREST",
      "CHESTP",
      "COMM BUILD FIRE",
      "COMM FIRE ALARM",
      "FALLS ACCID",
      "HAZ CONDIT SPILL",
      "HOUSE FIRE",
      "MENTAL EMOTION",
      "MUTUAL AID E",
      "MUTUAL AID F",
      "MVA ENTRAP",
      "MVA HAZ",
      "MVA INJ",
      "SEIZURES",
      "SICK",
      "STROKE",
      "SYNCOPAL EPI",
      "UNCONS UNRESP",
      "UNKNW FIRE",
      "VEH FIRE"
  );
  
  private String[] MWORD_STREET_LIST = new String[]{
      "A P HILL",
      "BOYDTON PLANK",
      "SQUIRREL LEVEL",
      "WEST WASHINGTON"
 };

  private static final String[] CITY_LIST = new String[]{

    // Towns
    "MCKENNEY",

    // Unincorporated communities
    "AMMON",
    "CARSON",
    "CHURCH ROAD",
    "DEWITT",
    "DARVILS",
    "DINWIDDIE",
    "FORD",
    "NORTH DINWIDDIE",
    "SUTHERLAND",
    "WILSONS",
    
    // Independent cities
    "PETERSBURG"

  };
}
