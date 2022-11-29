package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class SCOconeeCountyCParser extends DispatchA72Parser {

  public SCOconeeCountyCParser() {
    super("OCONEE COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "senecapolicedispatch@gmail.com,itsupport@netmds.com";
  }
}
