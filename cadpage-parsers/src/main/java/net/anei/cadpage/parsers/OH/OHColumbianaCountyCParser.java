package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class OHColumbianaCountyCParser extends DispatchA71Parser {

  public OHColumbianaCountyCParser() {
    super("COLUMBIANA COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "sunsrv@sundance-sys.com,CCSO_CADUser";
  }
}
