package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class KYFranklinCountyBParser extends DispatchA72Parser {

  public KYFranklinCountyBParser() {
    super("FRANKLIN COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "drambo@frankfort.ky.gov,geoconextest@911email.net,franklincoky@911email.net";
  }
}
