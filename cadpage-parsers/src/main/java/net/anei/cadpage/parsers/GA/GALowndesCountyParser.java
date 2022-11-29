package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class GALowndesCountyParser extends DispatchA57Parser {

  public GALowndesCountyParser() {
    super("LOWNDES COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "dispatch@lowndescounty.com";
  }

}
