package net.anei.cadpage.parsers.TX;


import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Nacogdoches County, TX
 */
public class TXNacogdochesCountyParser extends DispatchA19Parser {
  
  public TXNacogdochesCountyParser() {
    super("NACOGDOCHES COUNTY", "TX");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }
}
