package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA50Parser;

/**
 * Newport News, VA
 */
public class VANewportNewsParser extends DispatchA50Parser {
  
  public VANewportNewsParser() {
    super(null, CITY_CODES, "NEWPORT NEWS", "VA");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@nngov.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!subject.equals("Newport News 911")) return false;
    return super.parseMsg(body, data);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "NN",   "NEWPORT NEWS"
  });
}
