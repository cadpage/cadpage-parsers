package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class PALawrenceCountyBParser extends DispatchH03Parser {

  public PALawrenceCountyBParser() {
    this("LAWRENCE COUNTY", "PA");
  }

  PALawrenceCountyBParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter() {
    return "C@leoc.net,@RCAD911.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("JACKSON")) return "COOPERSTOWN";
    return city;
  }
}
