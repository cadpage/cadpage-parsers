package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MODekalbCountyParser extends DispatchBCParser {

  public MODekalbCountyParser() {
    super("DEKALB COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "DEKALBCODISPATCH@OMNIGO.COM,LACLEDECOES@OMNIGO.COM";
  }

}
