package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOWashingtonCityParser extends DispatchA33Parser {

  public MOWashingtonCityParser() {
    super("WASHINGTON", "MO", A33_X_ADDR_EXT);
  }

  @Override
  public String getLocName() {
    return "Washington City, MO";
  }

  @Override
  public String getFilter() {
    return "DISPATCH@OMNIGO.COM,DISPATCH@CI.WASHINGTON.MO,noreply@omnigo.com";
  }

  private static Pattern ST_EXT_PTN = Pattern.compile("\\b(MO)\\d\\b");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = ST_EXT_PTN.matcher(body).replaceAll("$1");
    return super.parseMsg(body, data);
  }

}
