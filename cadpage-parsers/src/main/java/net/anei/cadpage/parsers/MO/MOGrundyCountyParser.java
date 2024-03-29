package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOGrundyCountyParser extends DispatchBCParser {

  public MOGrundyCountyParser() {
    super("GRUNDY COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "TRENTONPOLICEDEPARTMENT@OMNIGO.COM,noreply@omnigo.com";
  }

}
