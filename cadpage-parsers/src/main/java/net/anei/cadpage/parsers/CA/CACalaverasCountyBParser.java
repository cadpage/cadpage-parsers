package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA69Parser;

/**
 * Calaveras County, CA (B)
 */
public class CACalaverasCountyBParser extends DispatchA69Parser {

  public CACalaverasCountyBParser() {
    this("CALAVERAS COUNTY", "CA");
  }

  protected CACalaverasCountyBParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "CACalaverasBCounty";
  }

  @Override
  public String getFilter() {
    return "tcucad@FIRE.CA.GOV";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = data.strCity.replace('_', ' ');
    return true;
  }

}
