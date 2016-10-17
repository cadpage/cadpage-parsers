package net.anei.cadpage.parsers.SC;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;

/**
 * Georgetown County, SC (B)
 */
public class SCGeorgetownCountyBParser extends DispatchPrintrakParser {
  
  public SCGeorgetownCountyBParser() {
    super(CITY_CODES, "GEORGETOWN COUNTY", "SC");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "CAD:");
    return super.parseMsg(body, data);
  }

  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("BY-PASS",  "BYPASS");
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "GC",  "GEORGETOWN COUNTY",
      "MI",  "MURRELLS INLET" 
  });
}
