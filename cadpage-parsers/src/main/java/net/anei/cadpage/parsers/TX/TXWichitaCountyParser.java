package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA82Parser;

public class TXWichitaCountyParser extends DispatchA82Parser {

  public TXWichitaCountyParser() {
    super("WICHITA COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "CADAlert@burkburnett.org";
  }
}
