package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GAPikeCountyParser extends DispatchB2Parser {
  
  public GAPikeCountyParser() {
    super("PIKE COUNTY E-911:||PIKE_COUNTY_E-911:", "PIKE COUNTY", "GA", B2_FORCE_CALL_CODE | B2_CROSS_FOLLOWS);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "PIKE_COUNTY_E-911@bellsouth.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldEnd(body, " 770");
    return super.parseMsg(body, data);
  }
  
  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = field.replace('@', '&').replace(" AT ", " & ");
    return super.parseAddrField(field, data);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = AREA_OF_PTN.matcher(addr).replaceAll("");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern AREA_OF_PTN = Pattern.compile("\\bAREA OF\\b", Pattern.CASE_INSENSITIVE);
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BLANTON MILL",
    "COUNTY FARM",
    "DREW ALLEN",
    "EPPINGER BRIDGE",
    "FLAT SHOALS",
    "HAGANS MOUNTAIN",
    "IRISH HILL",
    "LIFSEY SPRINGS",
    "TWIN OAKS",
    "WILLIAMSON ZEBULON"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "POSS DEAD PERSON",
      "PERSON INJURED",
      "SUICIDE ATTEMPTS",
      "ACCIDENT WITH INJURIES",
      "ACCIDENT WITH UNKNOWN",
      "EMS/ RESPONDER",
      "FIRE ALARM",
      "FIRE GENERIC (TYPE)",
      "STRUCTURE FIRE",
      "VEHICLE FIRE",
      "SMOKE"
  );
}
