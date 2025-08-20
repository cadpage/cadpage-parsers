package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class TXGuadalupeCountyParser extends DispatchA19Parser {

  public TXGuadalupeCountyParser() {
    super("GUADALUPE COUNTY","TX");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  public String getFilter() {
    return "rapidnotifications@seguintexas.gov,ibridge@mail.police365.com,@mail.365labs.com";
  }
}
