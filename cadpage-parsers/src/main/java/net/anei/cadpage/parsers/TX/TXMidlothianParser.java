package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXMidlothianParser extends DispatchA18Parser {
  
  public TXMidlothianParser() {
    super(CITY_LIST, "MIDLOTHIAN","TX");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
 
  @Override
  public String getFilter() {
    return "need@midlothian.tx.us";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("NONE")) data.strCity = "";
    return true;
  }
  
  private static String[] CITY_LIST = new String[]{
      
      "NONE",

      // Cities
      "BARDWELL",
      "CEDAR HILL",
      "ENNIS",
      "FERRIS",
      "GLENN HEIGHTS",
      "MANSFIELD",
      "MAYPEARL",
      "MIDLOTHIAN",
      "OVILLA",
      "PECAN HILL",
      "RED OAK",
      "WAXAHACHIE",

      // Towns
      "ALMA",
      "GARRETT",
      "ITALY",
      "MILFORD",
      "OAK LEAF",
      "PALMER",
      "VENUS",

      // Unincorporated communities
      "AVALON",
      "FORRESTON",
      "IKE",
      "RANKIN",
      "ROCKETT",
      "TELICO",
      "BRISTOL",
      "CRISP",
      "BARDWELL",
      
      // Dallas County
      "DESOTO",
      "DUNCANVILLE",
      "GRAND PRAIRIE",
      "HUTCHINS",
      "LANCASTER"

  };
}
