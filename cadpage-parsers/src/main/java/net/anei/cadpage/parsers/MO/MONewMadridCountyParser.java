package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchC04Parser;

public class MONewMadridCountyParser extends DispatchC04Parser {

  public MONewMadridCountyParser() {
    this("NEW MADRID COUNTY", "MO");
  }

  MONewMadridCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "MONewMadridCounty";
  }

  @Override
  public String getFilter() {
    return "NMPDCAD@NEWMADRIDPD.CO";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\nThis message");
    if (pt >= 0) body = body.substring(0,pt).trim();    return super.parseMsg(subject, body, data);
  }

}
