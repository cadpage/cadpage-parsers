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
      "ABDOMINAL",
      "ABDOMINAL PAIN",
      "ALLERGIC REACTION",
      "ASSAULT RAPE RES",
      "BACK PAIN",
      "BLEEDING (NON-TRAUMATIC)",
      "BREATHING DIFF",
      "BREATHING DIFFICULTY",
      "BRUSH FIRE",
      "CARD RESP ARREST",
      "CHESTP",
      "CHESTPAIN/HEART PROBLEM",
      "CHEST PAIN/HEART PROBLEMS",
      "COMM BUILD FIRE",
      "COMM FIRE ALARM",
      "DIABET",
      "EXCESS HEAT",
      "FALLS",
      "FALLS ACCID",
      "HAZ CONDIT SPILL",
      "HOUSE FIRE",
      "ILLEGAL BURN",
      "MENTAL EMOTION",
      "MUTUAL AID E",
      "MUTUAL AID F",
      "MVA ENTRAP",
      "MVA HAZ",
      "MVA INJ",
      "OVERDOSE/POISONING",
      "PUBSER",
      "RESID FIRE ALARM",
      "SEIZURES",
      "SICK",
      "STROKE",
      "SYNCOPAL EPISODE",
      "TRAUMA INJURY",
      "UNCONSCIOUS/UNRESPONSIVE/ALT LOC",
      "UNKNW FIRE",
      "UNKN PROB MAN DWN RES",
      "UNCONSCIOUS/UNRESPONSVIE EPISODE",
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
    "PETERSBURG",
    "TUCKER"

  };
}
