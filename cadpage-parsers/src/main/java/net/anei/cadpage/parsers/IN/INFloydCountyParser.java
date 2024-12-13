package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INFloydCountyParser extends DispatchA19Parser {

  public INFloydCountyParser() {
    super("FLOYD COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "911@FCSDIN.NET,@NAPD.COM";
  }
}
