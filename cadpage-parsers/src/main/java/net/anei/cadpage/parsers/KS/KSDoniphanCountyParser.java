package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSDoniphanCountyParser extends DispatchA25Parser {

  public KSDoniphanCountyParser() {
    super("DONIPHAN COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "alerts@doniphanso.org";
  }

}
