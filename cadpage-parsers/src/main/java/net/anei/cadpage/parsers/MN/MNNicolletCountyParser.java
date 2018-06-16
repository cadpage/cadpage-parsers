package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

public class MNNicolletCountyParser extends DispatchA43Parser {
  
  public MNNicolletCountyParser() {
    super("NICOLLET COUNTY", "MN");
    setupSpecialStreets("CHAPEL VIEW");
  }
}
