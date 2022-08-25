package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;

public class TXEllisCountyBParser extends DispatchA18Parser {
  
  public TXEllisCountyBParser() {
    super(CITY_LIST, "ELLIS COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "crimes@ferristexas.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nThis email was generated");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }
  
  private static final String[] CITY_LIST = new String[]{
    
        // Cities
        "BARDWELL",
        "CEDAR HILL",
        "ENNIS",
        "FERRIS",
        "GLENN HEIGHTS",
        "GRAND PRAIRIE",
        "MANSFIELD",
        "MAYPEARL",
        "MIDLOTHIAN",
        "OAK LEAF",
        "OVILLA",
        "PECAN HILL",
        "RED OAK",
        "WAXAHACHIE",
        
        // Towns
        "ALMA",
        "GARRETT",
        "ITALY",
        "MILFORD",
        "PALMER",
        "VENUS",
        
        // Census-designated place
        "BRISTOL",
        
        // Unincorporated communities
        "AUBURN",
        "AVALON",
        "CRISP",
        "FORRESTON",
        "IKE",
        "INDIA",
        "RANKIN",
        "ROCKETT",
        "TELICO",
        "TRUMBULL"
  };
}
