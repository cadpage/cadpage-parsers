package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class PAIndianaCountyParser extends SmartAddressParser {
  
  public PAIndianaCountyParser() {
    super(CITY_LIST, "INDIANA COUNTY", "PA");
    setFieldList("CALL PRI ADDR APT CITY PLACE X CH ID GPS UNIT");
    setupMultiWordStreets(MWORD_CITY_LIST);
    removeWords("BUS", "DRIVE");
  }
  
  @Override
  public String getFilter() {
    return "911@INDIPAGE.LC";
  }
  
  private static final Pattern MASTER = Pattern.compile("(.*?):(?: +|([A-Z]+); *)?(.*?) FIRE OPS:(.*) INC:(.*)");
  private static final Pattern CITY_EXT_PTN = Pattern.compile("(?:BORO(?:UGH)?|TWP)\\b");
  private static final Pattern CROSS_SFX_PTN = Pattern.compile("(.*) *\\b(ROUTE|PRIVATEROAD)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Automatic R&R Notification") && !subject.equals("Text Message")) return false;

    body = body.replace("\n", " ");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1).trim();
    data.strPriority = getOptGroup(match.group(2));
    String addr = match.group(3).trim();
    data.strChannel = match.group(4).trim();
    String idUnit = match.group(5);
    
    int pt = addr.indexOf(',');
    if (pt < 0) return false;
    parseAddress(addr.substring(0,pt).trim(), data);
    addr = addr.substring(pt+1).trim();

    parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, addr, data);
    addr = getLeft();
    match = CITY_EXT_PTN.matcher(addr);
    if (match.lookingAt()) {
      addr = addr.substring(match.end()).trim();
      if (match.group().equals("TWP")) data.strCity += " TWP";
    }
    
    pt = addr.indexOf(" - ");
    if (pt >= 0) {
      data.strPlace = addr.substring(0,pt).trim();
      data.strCross = addr.substring(pt+3).trim();
    } else {
      String crossExt = "";
      pt = addr.indexOf(',');
      if (pt >= 0) {
        crossExt = addr.substring(pt+1).trim();
        addr = addr.substring(0,pt).trim();
      }
      parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS | FLAG_ANCHOR_END, addr, data);
      if (data.strCross.isEmpty()) {
        data.strCross = addr;
        data.strPlace = "";
      }
      data.strCross = append(data.strCross, ", ", crossExt);
    }
    
    match = CROSS_SFX_PTN.matcher(data.strPlace);
    if (match.matches()) {
      data.strPlace = match.group(1).trim();
      data.strCross = append(match.group(2), " ", data.strCross);
    }
    
    pt = Math.max(idUnit.lastIndexOf(']'), idUnit.lastIndexOf(')'))+1;
    if (pt <= 0) return false;
    if (pt < idUnit.length() && idUnit.charAt(pt) == ' ') {
      data.strCallId = idUnit.substring(0,pt);
      idUnit = idUnit.substring(pt).trim();
      data.strUnit = setGPSLoc(idUnit, data);
    } else {
      data.strCallId = idUnit;
    }
    
    return true;
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, COUNTY_CODES);
  }
  
  private static final String[] MWORD_CITY_LIST = new String[] {
      "ALLEN BRIDGE",
      "ANTHONY RUN",
      "BARCLAY HEIGHTS",
      "BARR SLOPE",
      "BEARTRACKS HILL",
      "BEN AVON",
      "BEN FRANKLIN",
      "BETHEL CHURCH",
      "BLAIRSVILLE CLUB",
      "BLUE BIRD",
      "BUCK RUN",
      "CHERRY TREE",
      "CHESTNUT RIDGE",
      "CHESTNUT RIDGE PENN",
      "CHRISTY PARK",
      "CLAY PIKE",
      "CORPORATE CAMPUS",
      "CUSH CREEK",
      "DECKERS POINT",
      "DEER RUN",
      "EAST RUN",
      "FIRE ACADEMY",
      "FLEMMING SUMMIT",
      "GLEN CAMPBELL",
      "GLOBAL KENDALL",
      "GLORY STATION",
      "GRANGE HALL",
      "GREEN VALLEY",
      "HERITAGE RUN",
      "HILL TOP",
      "HOG HOLLOW",
      "HOME FARM",
      "HOOD SCHOOL",
      "HUMMING BIRD",
      "HUNTERS RIDGE",
      "HUTCHISON HOLLOW",
      "INDIAN SPRINGS",
      "INDIANA ANDERSON",
      "INDIANA PLAZA",
      "JESSIE PENROSE",
      "MCCORMICKS BRIDGE",
      "MEADOW WOOD",
      "MILL RUN",
      "MOBILE MANOR",
      "MOCKING BIRD",
      "MOUNTAIN CREEK LAUREL",
      "MOUNTAIN VIEW",
      "MULLIGAN HILL",
      "NORTH MAIN",
      "NUMBER ELEVEN",
      "PEARCE HOLLOW",
      "PENN VIEW",
      "PICKERING RUN",
      "PINE FLATS",
      "PINE RIDGE",
      "PINE VALE",
      "PINES ESTATES",
      "PIONEER LAKE",
      "POWER PLANT",
      "PURCHASE LINE",
      "RED MILL",
      "RED OAK",
      "REGENCY SQUARE",
      "RESORT PLAZA",
      "RIDGE VIEW",
      "ROBIN HILL",
      "SAND STONE",
      "SHADY GROVE",
      "SHARPS HILL",
      "SOUTH MAIN",
      "ST STANS",
      "STERLING HILLS",
      "STILES STATION",
      "STONE RIDGE",
      "SYLVAN ACRES",
      "TIMBER SPRINGS",
      "WEST LEBANON",
      "WEST PIKE",
      "WEST PIKE OFF RAMP",
      "WEST PIKE ON RAMP",
      "WHITE OAK",
      "WHITE STATION",
      "WILLIAM PENN",
      "WINDY RIDGE",
      "WOLF RUN",
      "WOOD CREST",
      "YANKEE HILL"

  };

  private static final String[] CITY_LIST = new String[] {
      
      // Boroughs
      "ARMAGH",
      "BLAIRSVILLE",
      "CHERRY TREE",
      "CLYMER",
      "CREEKSIDE",
      "ERNEST",
      "GLEN CAMPBELL",
      "HOMER CITY",
      "INDIANA",
      "MARION CENTER",
      "PLUMVILLE",
      "SALTSBURG",
      "SHELOCTA",
      "SMICKSBURG",
      
      // Townships
      "ARMSTRONG",
      "BANKS",
      "BLACK LICK",
      "BRUSH VALLEY",
      "BUFFINGTON",
      "BURRELL",
      "CANOE",
      "CENTER",
      "CHERRYHILL",
      "CONEMAUGH",
      "EAST MAHONING",
      "EAST WHEATFIELD",
      "GRANT",
      "GREEN",
      "MONTGOMERY",
      "NORTH MAHONING",
      "PINE",
      "RAYNE",
      "SOUTH MAHONING",
      "WASHINGTON",
      "WEST MAHONING",
      "WEST WHEATFIELD",
      "WHITE",
      "YOUNG",

      // Census-designated places
      "ALVERDA",
      "BLACK LICK",
      "CHEVY CHASE HEIGHTS",
      "COMMODORE",
      "CORAL",
      "DICKSONVILLE",
      "GRACETON",
      "HEILWOOD",
      "JACKSONVILLE",
      "LUCERNE MINES",
      "ROBINSON",
      "ROSSITER",

      // Unincorporated communities
      "ARCADIA",
      "CLARKSBURG",
      "COVODE",
      "DILLTOWN",
      "DIXONVILLE",
      "GIPSY",
      "HOME",
      "ISELIN",
      "JEWTOWN",
      "LOCUST",
      "LOOP",
      "MENTCLE",
      "NOLO",
      "REXIS",
      "ROCHESTER MILLS",
      "STARFORD",
      "STRONGSTOWN",
      "WEHRUM",
      "WEST LEBANON",

      // Armstrong County
      "ARMS CO",

      //  Cambra County
      "CAMB CO",
      
      // Westmoreland County
      "WEST CO"
  };
  
  private static final Properties COUNTY_CODES = buildCodeTable(new String[] {
      "ARMS CO",  "ARMSTRONG COUNTY",
      "CAMB CO",  "CAMBRIA COUNTY",
      "WEST CO",  "WESTMORELAND COUNTY"
  });
}
