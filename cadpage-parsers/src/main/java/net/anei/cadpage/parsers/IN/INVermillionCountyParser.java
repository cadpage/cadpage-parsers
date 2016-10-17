package net.anei.cadpage.parsers.IN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class INVermillionCountyParser extends DispatchA29Parser {
  
  public INVermillionCountyParser() {
    super(CITY_LIST, "VERMILLION COUNTY", "IN");
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "sbfd702@yahoo.com";
  }

  private static final Pattern ST_BERN_PTN = Pattern.compile("\\bST. BERN\\b");
  @Override
  public boolean parseMsg(String body, Data data) {
    body = ST_BERN_PTN.matcher(body).replaceAll("ST_BERN");
    body = body.replace("BLK DIMD", "BLK_DIMD");
    if (!super.parseMsg(body, data)) return false;
    int pt = data.strAddress.indexOf(',');
    if (pt >= 0) data.strAddress = data.strAddress.substring(0,pt).trim();
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "RESCUE/AMBULANCE",
      "FIRE-BRUSH",
      "FIRE-GENERAL",
      "FIRE-STRUCTURE",
      "FIRE-VEHICLE",
      "FIRE ALARM",
      "MEDICAL"
  );

  private static final String[] CITY_LIST = new String[]{
    "ALTA",
    "BLANFORD",
    "BONO",
    "CAYUGA",
    "CENTENARY",
    "CLINTON",
    "DANA",
    "EUGENE",
    "FAIRVIEW PARK",
    "FLAT IRON",
    "GESSIE",
    "HIGHLAND",
    "HILLSDALE",
    "JONESTOWN",
    "KLONDYKE",
    "NEWPORT",
    "PERRYSVILLE",
    "QUAKER",
    "RANDALL",
    "RILEYSBURG",
    "SAINT BERNICE",
    "SUMMIT GROVE",
    "SYNDICATE",
    "ST BERNICE",
    "SUMMIT GROVE",
    "TORONTO",
    "TREE SPRING",
    "UNIVERSAL",
    
    "CLINTON TWP",
    "EUGENE TWP",
    "HELT TWP",
    "HIGHLAND TWP",
    "VERMILLION TWP"
  };
}
