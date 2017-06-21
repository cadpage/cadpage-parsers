package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA69Parser;

/**
 * Placer County, CA
 */
public class CAPlacerCountyAParser extends DispatchA69Parser {
  
  public CAPlacerCountyAParser() {
    this("PLACER COUNTY", "CA");
  }
  
  protected CAPlacerCountyAParser(String defCity, String defState) {
    super(defCity, defState);
  }
  
  @Override
  public String getAliasCode() {
    return "CAPlacerCounty";
  }
  
  @Override
  public String getFilter() {
    return "NEUCAD@FIRE.CA.GOV";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject,  body, data)) return false;
    data.strSource = data.strCity.replace(' ', '_');
    data.strCity = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("CITY", "SRC");
  }
}
