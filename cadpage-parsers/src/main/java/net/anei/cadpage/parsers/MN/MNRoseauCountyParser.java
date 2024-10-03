package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MNRoseauCountyParser extends DispatchA19Parser {

  public MNRoseauCountyParser() {
    super("ROSEAU COUNTY", "MN");
  }

  @Override
  public String getFilter() {
    return "rapid.notification@co.roseau.mn.us";
  }
}
