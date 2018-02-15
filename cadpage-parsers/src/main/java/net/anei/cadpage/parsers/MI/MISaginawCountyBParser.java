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
    if (!subject.equals("TITTABAWASSEE FIRE DEPT")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "20", "FREELAND",
      "25", "FREELAND"
  });
}