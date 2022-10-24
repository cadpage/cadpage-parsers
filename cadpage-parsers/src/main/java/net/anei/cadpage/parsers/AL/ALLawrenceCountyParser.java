package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA89Parser;

public class ALLawrenceCountyParser extends DispatchA89Parser {

  public ALLawrenceCountyParser() {
    super("LAWRENCE COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "dispatch@lawrenceal911.info";
  }

}
