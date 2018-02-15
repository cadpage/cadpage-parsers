package net.anei.cadpage.parsers.MI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class MISaginawCountyBParser extends DispatchH03Parser {
  
  public MISaginawCountyBParser() {
    super(CITY_CODES, "SAGINAW COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "@saginawcounty.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    data.strSource = subject;
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strCall.equals(data.strCode)) data.strCode = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "06", "BRIDGEPORT TWP",
      "20", "TITTABAWASSEE TWP",
      "22", "SPAULDING TWP",
      "25", "FREELAND"
  });
}