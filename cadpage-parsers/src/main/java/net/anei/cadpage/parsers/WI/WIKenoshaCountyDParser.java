package net.anei.cadpage.parsers.WI;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class WIKenoshaCountyDParser extends DispatchA57Parser {

  public WIKenoshaCountyDParser() {
    super("KENOSHA COUNTY", "WI");
  }

  WIKenoshaCountyDParser(String defState, String defCity) {
    super(defState, defCity);
    setupCities(WIKenoshaCountyParser.CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "dispatch@kenoshajs.org";
  }

  @Override
  public String getAliasCode() {
    return "WIKenoshaCountyD";
  }

  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    if (!super.parseMsg(body, data)) return false;

    if (STATE_PTN.matcher(data.strCity).matches()) {
      data.strState = data.strCity;
      String addr = data.strAddress;
      data.strAddress = data.strCity = "";
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
    }

    int pt = data.strCross.lastIndexOf("Add");
    if (pt >= 0) {
      if ("Additional Location Info:".startsWith(data.strCross.substring(pt))) {
        data.strCross = data.strCross.substring(0,pt).trim();
      }
    }
    return true;
  }
}
