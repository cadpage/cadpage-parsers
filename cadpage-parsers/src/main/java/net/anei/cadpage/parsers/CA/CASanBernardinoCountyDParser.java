package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.StandardCodeTable;

/**
 * San Bernardino County, CA (B)
 * Obsolete - replace with version (D)
 */
public class CASanBernardinoCountyDParser extends SmartAddressParser {
  
  public CASanBernardinoCountyDParser() {
    super("SAN BERNARDINO COUNTY", "CA");
    setFieldList("UNIT MAP APT PLACE ADDR CITY CODE CALL ID");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@csb.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private static final Pattern DIR_DOUBLE_BLANK_PTN = Pattern.compile(" +([NSEW])  +");
  private static final Pattern MASTER = Pattern.compile("(?:([ A-Z0-9]+)  )?(?:(S\\d{4}) )?(.*) ([A-Z0-9]+) (\\d{4}-\\d{8})");
  private static final Pattern APT_PLACE_PTN = Pattern.compile("(\\d+[A-Z]?) +(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident")) return false;
    body = DIR_DOUBLE_BLANK_PTN.matcher(body).replaceAll(" $1 ");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = getOptGroup(match.group(1));
    data.strMap = getOptGroup(match.group(2));
    String addr = match.group(3);
    data.strCode = match.group(4);
    data.strCallId = match.group(5);
    
    int pt = addr.indexOf(" - ");
    String place = null;
    if (pt >= 0) {
      place = addr.substring(0,pt).trim();
      addr = addr.substring(pt+3).trim();
    }
    
    pt = addr.lastIndexOf(' ');
    if (pt >= 0) {
      String city = CITY_CODES.getProperty(addr.substring(pt+1).trim());
      if (city != null) {
        data.strCity = city;
        addr = addr.substring(0,pt).trim();
      }
    }
    
    parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, addr, data);
    if (place != null) {
      place = stripFieldStart(place, addr);
      match = APT_PLACE_PTN.matcher(place);
      if (match.matches()) {
        data.strApt = match.group(1);
        place = match.group(2);
      }
      data.strPlace = place;
    }
    
    String call = CALL_CODES.getCodeDescription(data.strCode);
    if (call == null) call = data.strCode;
    data.strCall = call;
    return true;
  }
  
  private static final CodeTable CALL_CODES = new StandardCodeTable();
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARB", "ARROW BEAR",
      "BBC", "BIG BEAR CITY",
      "BLO", "BLOOMINGTON",
      "BLU", "BLUE JAY",
      "CED", "CEDAR GLEN",
      "COL", "COLTON",
      "CPP", "CEDAR PINES PARK",
      "CRL", "CRESTLINE",
      "DEV", "DEVORE",
      "FON", "FONTANA",
      "GRT", "GRAND TERRACE",
      "GVL", "GRASS VALLEY",
      "HIG", "HIGHLAND",
      "LOM", "LOMA LINDA",
      "LYC", "LYTLE CREEK",
      "MEN", "MENTONE",
      "MUS", "MUSCOY",
      "ONT", "ONTARIO",
      "PAT", "PATTON",
      "RCC", "RANCHO CUCAMONGA",
      "RED", "REDLANDS",
      "RIA", "RIALTO",
      "RIM", "RIM FORREST",
      "RSP", "RUNNING SPRINGS",
      "SBC", "SAN BERNARDINO COUNTY",
      "SBO", "SAN BERNARDINO",
      "SIL", "SILVERWOOD LK",
      "SKY", "SKY FORREST",
      "SMI", "SAN MANUEL",
      "TPK", "TWIN PEAKS",
      "VOE", "VALLEY OF ENCHANTMENT",
      "YUC", "YUCAIPA"

  });
}
