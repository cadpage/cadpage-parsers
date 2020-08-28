package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ILCookCountyLParser extends DispatchA19Parser {

  public ILCookCountyLParser() {
    super("COOK COUNTY", "IL");
  }

  @Override
  public String getFilter() {
    return "MABAS22@CALCOMM911.org,MABAS22@calumetparkvillage.org";
  }

}
