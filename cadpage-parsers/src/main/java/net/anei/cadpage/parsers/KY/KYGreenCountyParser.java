package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYGreenCountyParser extends DispatchA27Parser {

  public KYGreenCountyParser() {
    super("GREEN COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "greensburg911@gmail.com,greensburgky@cissystem.com";
  }

}
