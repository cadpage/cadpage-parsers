package net.anei.cadpage.parsers.AZ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class AZPimaCountyParser extends SmartAddressParser {
  
  public AZPimaCountyParser() {
    super(CITY_LIST, "PIMA COUNTY", "AZ");
    setFieldList("ADDR CITY APT X PLACE CALL UNIT");
  }
  
  @Override
  public String getFilter() {
    return "35842";
  }
  
  private static final Pattern MASTER = Pattern.compile("([^()]*?) \\(([^()]*?)\\)(?: *\\([^()]*\\))?(?: ; (.*?))?-Dispatched-(.*?)-Dispatched-(.*)");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|SP|LOT) *(.*)", Pattern.CASE_INSENSITIVE);
  
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    String addr = match.group(1).trim();
    data.strCross = match.group(2).trim();
    data.strPlace = getOptGroup(match.group(3));
    data.strCall = match.group(4).trim();
    data.strUnit = match.group(5).trim();
    
    String apt = null;
    int pt = addr.indexOf('#');
    if (pt >= 0) {
      apt = addr.substring(pt+1).trim();
      addr = addr.substring(0, pt).trim();
      match = APT_PTN.matcher(apt);
      if (match.matches()) apt = match.group(1);
    }
    
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
    if (apt != null) data.strApt = append(data.strApt, "-", apt);
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "SOUTH TUCSON",
      "TUCSON",

      // Towns
      "MARANA",
      "ORO VALLEY",
      "SAHUARITA",

      // Census-designated places
      "AJO",
      "AK CHIN",
      "ALI CHUK",
      "ALI CHUKSON",
      "ALI MOLINA",
      "ANEGAM",
      "ARIVACA",
      "ARIVACA JUNCTION",
      "AVRA VALLEY",
      "CASAS ADOBES",
      "CATALINA",
      "CATALINA FOOTHILLS",
      "CHIAWULI TAK",
      "CHARCO",
      "COMOBABI",
      "CORONA DE TUCSON",
      "COWLIC",
      "DREXEL HEIGHTS",
      "ELEPHANT HEAD",
      "FLOWING WELLS",
      "GREEN VALLEY",
      "GU OIDAK",
      "HAIVANA NAKYA",
      "KO VAYA",
      "LITTLETOWN",
      "MAISH VAYA",
      "NELSON",
      "NOLIC",
      "PICTURE ROCKS",
      "PIMACO TWO",
      "PISINEMO",
      "RILLITO",
      "RINCON VALLEY",
      "SAN MIGUEL",
      "SANTA ROSA",
      "SELLS",
      "SOUTH KOMELIK",
      "SUMMERHAVEN",
      "SUMMIT",
      "TANQUE VERDE",
      "THREE POINTS",
      "TOPAWA",
      "TUCSON ESTATES",
      "VAIL",
      "VALENCIA WEST",
      "VENTANA",
      "WAHAK HOTRONTK",
      "WHY",
      "WILLOW CANYON",

      // Indian reservations
      "PASCUA YAQUI",
      "SAN XAVIER",
      "TOHONO O'ODHAM",

      // Other communities
      "DREXEL-ALVERNON",
      "EAST SAHUARITA",
      "KENTUCKY CAMP",
      "LUKEVILLE",
      "REDINGTON",
      "SASABE",
      "TORTOLITA",
      
      // Santa Cruz County
      "AMADO"
  };

}
