package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Lampasas County, TX
 */
public class TXLampasasCountyParser extends DispatchA19Parser {

  public TXLampasasCountyParser() {
    super("LAMPASAS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "ripnrun@co.lampasas.tx.us";
  }
}
