package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class GAOconeeCountyParser extends SmartAddressParser {

  public GAOconeeCountyParser() {
    super("OCONEE COUNTY", "GA");
    setFieldList("PHONE CODE CALL ADDR APT X INFO MAP ID");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets(
        "BARROW COUNTY LINE",
        "BENT TREE PT");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (!body.startsWith("OCSO_E911:")) return false;
    body = body.substring(10).trim();

    boolean hasPhone = body.startsWith("Return Phone:");
    if (hasPhone) body = body.substring(13).trim();
    Parser p = new Parser(body);
    if (hasPhone) data.strPhone = p.get(' ');
    data.strCode = p.get(' ');

    data.strCallId = p.getLastOptional(" Cad:");
    String map =  p.getLastOptional("Map: Grids:");
    if (!map.contentEquals("0,0")) data.strMap =  map;
    body = p.get();
    if (body.length() == 0) return false;

    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_IGNORE_AT | FLAG_CROSS_FOLLOWS, body, data);
    body = stripFieldStart(getLeft(), "Bldg");

    if (isValidAddress(body)) {
      data.strCross = body;
    } else {
      data.strSupp = body;
    }

    return true;
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT (INJURIES",
      "DOMESTIC PHYSICAL",
      "DOMESTIC PHYSICAL",
      "DOMESTIC VERBAL",
      "FIRE",
      "FIRE ALARM",
      "GAS LEAK",
      "INJURED PERSON",
      "LIFT ASSITANCE",
      "MEDICAL ALARM",
      "SICK PERSON",
      "SMOKE",
      "TREE DOWN IN POWER LINES",
      "TREE DOWN ON ROAD"
  );

  private static final String[] MWORD_STREET_LIST = new String[] {
      "BARNETT SHOALS",
      "COLHAM FERRY",
      "DANIELLS BRIDGE",
      "DIALS MILL",
      "EPPS BRIDGE",
      "EXPERIMENT STATION",
      "FLAT ROCK",
      "HARDEN HILL",
      "HIGH SHOALS",
      "HODGES MILL",
      "HOG MOUNTAIN",
      "HOLLOW CREEK",
      "JENNINGS MILL",
      "JIMMY DANIEL",
      "JIMMY DANIELS",
      "KNOB CREEK",
      "LAKE WELBROOK",
      "LANE CREEK",
      "MALCOM BRIDGE",
      "MARS HILL",
      "MOORES FORD",
      "NORTH BURSON",
      "OLIVER BRIDGE",
      "PRICE MILL",
      "RIVER'S EDGE",
      "RIVERS EDGE",
      "SCARLET OAK",
      "VIRGIL LANGFORD"

  };
}
