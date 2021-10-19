package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INHowardCountyBParser extends DispatchSPKParser {

  public INHowardCountyBParser() {
    super("HOWARD COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "HowardCoCAD@ipsc.in.gov";
  }

}
