package net.anei.cadpage.parsers.WA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA5Parser;

/**
 * Walla Walla County, WA
 */
public class WAWallaWallaCountyParser extends DispatchA5Parser {
  
  public WAWallaWallaCountyParser() {
    super(CITY_CODES, "WALLA WALLA COUNTY", "WA");
  }
  
  @Override
  public String getFilter() {
    return "@WALLAWALLAWA.GOV";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCall.equals("E ProQA")) data.strCall = "E Medical";
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "College Pl",     "COLLEGE PLACE",
      "WW City",        "WALLA WALLA",
      "WW County",      "WALLA WALLA COUNTY"
  });
}