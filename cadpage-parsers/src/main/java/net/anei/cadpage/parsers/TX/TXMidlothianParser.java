package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Pattern;

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
    return "need@midlothian.tx.us,Crimes.Alerts@midlothian.tx.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("CAUTION: ")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      body = body.substring(pt+1).trim();
    }
    if (!super.parseMsg(subject, body, data)) return false;
    data.strAddress = cleanStreetName(data.strAddress);
    data.strCross = cleanStreetName(data.strCross);
    data.strCity = convertCodes(data.strCity, FIX_CITY_TABLE);
    return true;
  }
  
  private static final Pattern WOOD_ST_PTN = Pattern.compile("\\b(OAK|SHADOW) (WOOD)\\b", Pattern.CASE_INSENSITIVE);
  private String cleanStreetName(String addr) {
    return WOOD_ST_PTN.matcher(addr).replaceAll("$1$2");
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
      "WAXHACHIE",

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
  
  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "NONE",       "",
      "WAXHACHIE",  "WAXAHACHIE"
  });
}
