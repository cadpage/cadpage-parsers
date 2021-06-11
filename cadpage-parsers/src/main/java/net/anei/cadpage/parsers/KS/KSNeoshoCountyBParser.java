package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class KSNeoshoCountyBParser extends DispatchA72Parser {

  public KSNeoshoCountyBParser() {
    super("NEOSHO COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "chanute911@chanute.org";
  }

}
