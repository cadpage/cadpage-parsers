package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Kilgore, TX
 */
public class TXKilgoreParser extends DispatchA19Parser {

  public TXKilgoreParser() {
    super("KILGORE", "TX");
  }

  @Override
  public String getFilter() {
    return "kfddispatch@cityofkilgore.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
}