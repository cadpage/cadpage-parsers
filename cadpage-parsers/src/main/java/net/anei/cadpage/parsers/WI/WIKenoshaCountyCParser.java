package net.anei.cadpage.parsers.WI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class WIKenoshaCountyCParser extends DispatchProphoenixParser {

  public WIKenoshaCountyCParser() {
    super(CITY_CODES, WIKenoshaCountyParser.CITY_LIST, "KENOSHA COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return "CAD@plprairiewi.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return super.parseMsg(body, data);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "VPP", "Pleasant Prairie"
  });

}
