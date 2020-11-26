package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class MNMowerCountyBParser extends DispatchA27Parser {

  public MNMowerCountyBParser() {
    super("MOWER COUNTY", "MN");
  }

  @Override
  public String getFilter() {
    return "cadexport@co.mower.mn.us";
  }
}
