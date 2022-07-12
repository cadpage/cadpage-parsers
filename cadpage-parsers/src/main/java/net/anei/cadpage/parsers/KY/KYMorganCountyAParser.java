package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class KYMorganCountyAParser extends DispatchB2Parser {
  
  public KYMorganCountyAParser() {
    super("WLMC911:", CITY_LIST, "MORGAN COUNTY", "KY", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCross = stripFieldStart(data.strCross, "@");
    return true;
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    "LACY CREEK"
  };
  
  private static final String[] CITY_LIST = new String[]{
      "CANEY",
      "CANNEL CITY",
      "COTTLE",
      "CROCKETT",
      "DINGUS",
      "ELAMTON",
      "ELKFORK",
      "EZEL",
      "GRASSY CREEK",
      "LENOX",
      "MALONE",
      "MIMA",
      "MIZE",
      "MOON",
      "OPHIR",
      "RELIEF",
      "STACY FORK",
      "WEST LIBERTY",
      "WHITE OAK",
      "WRIGLEY",
      "YOCUM",
      "ZAG"

  };
  
  private CodeSet CALL_LIST = new CodeSet(
      "FALL",
      "INJURY ACCIDENT WITH WILDLIFE",
      "INJURY COLLISION",
      "LOST PERSON",
      "UNKNOWN PROBLEM"
  );
}
