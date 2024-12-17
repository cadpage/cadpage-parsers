package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INFloydCountyAParser extends DispatchA19Parser {

  public INFloydCountyAParser() {
    super("FLOYD COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "911@FCSDIN.NET,@NAPD.COM";
  }
}
