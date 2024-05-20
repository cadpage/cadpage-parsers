package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NJSalemCountyCParser extends DispatchA19Parser {

  public NJSalemCountyCParser() {
    this("SALEM COUNTY", "NJ");
  }

  public NJSalemCountyCParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "NJSalemCountyC";
  }

  @Override
  public String getFilter() {
    return "ctest@salemcountynj.onmicrosoft.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("STATION ALERT")) return false;

    int pt1 = body.indexOf("\nTIME - ");
    if (pt1 < 0) return false;
    int pt2 = body.indexOf("\nResponding Units:", pt1);
    if (pt2 < 0) return false;
    body = body.substring(pt1+1, pt2) + '\n' + body.substring(0, pt1) + body.substring(pt2);
    if (!super.parseMsg(subject, body, data)) return false;
    data.strUnit = stripFieldStart(data.strUnit, ",");
    return true;
  }
}
