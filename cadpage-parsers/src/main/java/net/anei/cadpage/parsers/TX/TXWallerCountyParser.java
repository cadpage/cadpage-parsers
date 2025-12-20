package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class TXWallerCountyParser extends DispatchA71Parser {

  public TXWallerCountyParser() {
    super("WALLER COUNTY", "TX",
          "( ADDR:ADDR! APT:APT! PLACE:PLACE! X-ST:X! MAP:MAP! SUB:MAP/L! NATURE:CALL! PRI:PRI! UNITS:UNIT! LAT:GPS1! LON:GPS2! ID:ID! NOTES:INFO! " +
          "| CALL:CALL! CALLS:SKIP! ADDR:ADDR! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! X:X INFO:INFO " +
          ") END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

}
