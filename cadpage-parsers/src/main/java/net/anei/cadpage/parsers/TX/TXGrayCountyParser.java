package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXGrayCountyParser extends DispatchA72Parser {

  public TXGrayCountyParser() {
    super("GRAY COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "rms-noreply@pampapd.com";
  }
}
