package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYSpencerCountyAParser extends DispatchSPKParser {
  
  public KYSpencerCountyAParser() {
    super("SPENCER COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "Ksp.NGCAD@ky.gov";
  }

}
