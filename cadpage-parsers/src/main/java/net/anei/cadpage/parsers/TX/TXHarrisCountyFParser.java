package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA91Parser;

public class TXHarrisCountyFParser extends DispatchA91Parser {

  public TXHarrisCountyFParser() {
    super("CAD PAGE", "HARRIS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "communications@baytown.org";
  }
}
