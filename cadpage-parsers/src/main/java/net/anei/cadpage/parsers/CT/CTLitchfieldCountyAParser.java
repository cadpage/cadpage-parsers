package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class CTLitchfieldCountyAParser extends SmartAddressParser {
  
  private static final Pattern MASTER1 = Pattern.compile("(.*) RESPOND TO (.*?)(?:,|,? (\\d{1,3}-[A-Z]-\\d{1,2}(?:-?[A-Z])?|(?:\\d{1,2}-)?(?:HOT|ALPHA|COLD)|\\d+|)) *(?::|--| -)(\\d\\d:\\d\\d)(?:(?: ([A-Z]\\d{2}-\\d+)|\\*\\*), *((?:[-+]?\\d+\\.\\d{4,}|0), *(?:[-+]?\\d+\\.\\d{4,}|0))| *\\(.*\\))?");
  private static final Pattern MISMATCH_PAREN_PTN = Pattern.compile("[^\\(\\)]*\\).*");
  
  private static final Pattern MASTER2 = Pattern.compile("(.+?)-(?!Dwelling)(.+)-(.*?) *\\*\\*\\* (\\d\\d:\\d\\d)---");
  private static final Pattern MAU_HILL = Pattern.compile("^(.*) MAUWEEHOO H(?:IL)?L (.*)$");
  private static final Pattern START_PAREN_PTN = Pattern.compile("^\\(.*?\\)");
  
  public CTLitchfieldCountyAParser() {
    super(CTLitchfieldCountyParser.CITY_LIST, "LITCHFIELD COUNTY", "CT");
    addExtendedDirections();
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupProtectedNames(PROTECTED_STREET_LIST);
    setupSpecialStreets("EAST ST", "WEST RD");
  }
  
  @Override
  public String getFilter() {
    return "@everbridge.net,@lcd911.com,89361";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    body = body.replace('\n', ' ');
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("SRC ADDR X PLACE APT CITY ST CALL CODE TIME ID GPS");
      data.strSource = match.group(1).trim();
      String sAddr = match.group(2).trim();
      data.strCode = getOptGroup(match.group(3));
      data.strTime = match.group(4);
      data.strCallId = getOptGroup(match.group(5));
      String gps = match.group(6);
      if (gps != null) setGPSLoc(gps, data);
      
      Parser p = new Parser(sAddr);
      data.strCall = p.getLast(", ");
      while (MISMATCH_PAREN_PTN.matcher(data.strCall).matches()) {
        String tmp = p.getLast(", ");
        if (tmp.length() == 0) return false;
        data.strCall = tmp + ", " + data.strCall;
      }
      data.strPlace = p.getLast(", ");
      sAddr = p.get();
      if (sAddr.length() == 0) return false;
      parseAddressField(sAddr, data);
      return true;
    }
    
    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ADDR X PLACE APT CITY ST PLACE TIME");
      
      data.strCall = match.group(1).trim();
      parseAddressField(match.group(2).trim(), data);
      data.strPlace = append(data.strPlace, " - ", match.group(3).trim());
      data.strTime = match.group(4);
      
      return true;
    }
    setFieldList("INFO");
    data.msgType = MsgType.GEN_ALERT;
    data.strSupp = body;
    return true;
  }

  private void parseAddressField(String sAddr, Data data) {
    Matcher match;
    parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, sAddr, data);
    String sPlace = stripFieldStart(getPadField(), "-");;
    
    
    // There is a street called MAUWEEHOO HILL (or HL) that just confuses
    // the heck out of the smart parser so we will make some special checks for it
    if (data.strPlace.length() == 0) {
      match = MAU_HILL.matcher(data.strAddress);
      if (match.find()) {
        data.strAddress = match.group(1) + " MAUWEEHOO HILL";
        sPlace = match.group(2);
      }
    }
    
    // OK, there can be some strange things in the pad field
    // A leading term in parens is an alternative street name that should be appended to the
    // address.  It will be removed from the map address
    match = START_PAREN_PTN.matcher(sPlace);
    if (match.find()) {
      data.strAddress = append(data.strAddress, " ", match.group());
      sPlace = sPlace.substring(match.end()).trim();
    }
    
    // What's left is an optional place name followed by an optional cross
    // street, which may have an alternate street name in parens
    // So first remove the tentative alternate street name
    String chkCross = sPlace;
    String extra = null;
    if (chkCross.endsWith(")")) {
      int pt = chkCross.indexOf('(');
      if (pt >= 0) {
        extra = chkCross.substring(pt);
        chkCross = chkCross.substring(0,pt).trim();
      }
    }
    
    // Now use the smart addresss parser to separate the place name from the cross street
    String savePlace = data.strPlace;
    data.strPlace = "";
    Result res = parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS | FLAG_ANCHOR_END, chkCross);
    if (res.isValid()) {
      res.getData(data);
    } else {
      data.strPlace = chkCross;
    }
    
    data.strPlace = stripFieldEnd(data.strPlace, "&");
    
    // Append the extra paren info the the cross street if we found one
    // or to the place name if we did not
    if (extra != null) {
      if (data.strCross.length() > 0) {
        data.strCross = append(data.strCross, " ", extra);
      } else {
        data.strPlace = append(data.strPlace, " ", extra);
      }
    }
    
    // If we had a place name before going into this logic, the field we
    //  just parsed as a place should really be an apartment
    if (savePlace.length() > 0) {
      data.strApt = data.strPlace;
      data.strPlace = savePlace;
    }
    CTLitchfieldCountyParser.fixCity(data);
  }
  
  private static final String[] PROTECTED_STREET_LIST = new String[]{
    "EAST CENTER",
    "EAST FARMS",
    "EAST LAKE",
    "EAST PEARL",
    "EAST VIEW",
    "NORTH MAIN",
    "SOUTH BEACH PLAIN",
    "SOUTH CHAPEL",
    "SOUTH MAIN",
    "SOUTH MAPLE",
    "WEST GRANBY",
    "WEST HILL",
    "WEST LAKE"
  };
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ABOVE ALL",
    "ALAIN WHITE",
    "ALVORD PARK",
    "BALDWIN HILL",
    "BEAVER BOG",
    "BEECH HILL",
    "BEL AIR",
    "BIRCH HILL",
    "BLACK BRIDGE",
    "BLUEBERRY HILL",
    "BOTTSFORD HILL",
    "BRANDY HILL",
    "BREEZY HILL",
    "BULLS BRIDGE",
    "BURR MOUNTAIN",
    "BURT HILL",
    "CAMPVILLE HILL",
    "CANDLEWOOD MOUNTAIN",
    "CARLSON RIDGE",
    "CARMEL HILL",
    "CARMEN HILL",
    "CEDAR HILL",
    "CEDAR RIDGE",
    "CHAPEL HILL",
    "CHESTNUT HILL",
    "CHESTNUT LAND",
    "CHURCH HILL",
    "CLATTER VALLEY",
    "COLEBROOK RIVER",
    "COLLEGE FARMS",
    "COLONIAL RIDGE",
    "COTTON HILL",
    "COUNTY LINE",
    "CROOKED FURROWS",
    "DAISY HILL",
    "DISH MILL",
    "DUTTON HILL",
    "EABOW BROOK",
    "EAST CENTER",
    "EAST FARMS",
    "EAST LAKE",
    "EAST PEARL",
    "EAST VIEW",
    "ENO HILL",
    "FARMINGTON RIVER",
    "FLAGG HILL",
    "FORGE HILL",
    "GLEN AYRE",
    "GOOSE GREEN",
    "GOSHEN EAST",
    "GREAT BROOK",
    "HALL MEADOW",
    "HARD HILL",
    "HARMONY HILL",
    "HARWINTON HEIGHTS",
    "HAYDEN HILL",
    "HENRY SANFORD",
    "HIGH VIEW",
    "HILL SIDE",
    "HODGES HILL",
    "HOLCOMB HILL",
    "HOSPITAL HILL",
    "HUT HILL",
    "INDIAN FIELD",
    "INDIAN MEADOW",
    "INDUSTRIAL PARK",
    "JACK CORNER",
    "KNIFE SHOP",
    "LAKE HARWINTON",
    "LAKE SHORE",
    "LAURELWOOD POND",
    "LEACH HOLLOW",
    "LEAD MINE BROOK",
    "LILLINONAH LAKE",
    "LILY POND",
    "LIME ROCK",
    "LIVERY POOL",
    "LOCUST HILL",
    "LONG MOUNTAIN",
    "LOON MEADOW",
    "MANGNOLIA HILL",
    "MAPLE HOLLOW",
    "MARSHALL LAKE",
    "MEADOW VIEW",
    "MIDDLE SCHOOL",
    "MILO COE",
    "MINE HILL",
    "MIST HILL",
    "MOUNT TOM",
    "MOUNTAIN VIEW",
    "NETTLETON HOLLOW",
    "NORTH MAIN",
    "OAK MEADOW",
    "ORCHARD REST",
    "PAPER MILL",
    "PARK LANE",
    "PARKER HILL",
    "PICKETT DISTRICT",
    "PINE RIDGE",
    "POPPLE SWAMP",
    "PROSPECT HILL",
    "QUAKER RIDGE",
    "RAMS GATE",
    "RATLUM MOUNTAIN",
    "RIDGE VIEW",
    "ROBERT LEATHER",
    "ROBIN HILL",
    "ROCK HALL",
    "RUDD POND",
    "SANDY BROOK",
    "SATANS KINGDOM",
    "SAW MILL HILL",
    "SAWYER HILL",
    "SCOVILLE HILL",
    "SELLECK HILL",
    "SHARON STATION",
    "SHEFFIELD HILL",
    "SHERMAN HILL",
    "SHERWOOD HILL",
    "SHINGLE MILL",
    "SMITH HILL",
    "SOUTH BEACH PLAIN",
    "SOUTH CHAPEL",
    "SOUTH MAIN",
    "SOUTH MAPLE",
    "SPENCER BROOK",
    "SPENCER HILL",
    "SPRING HILL",
    "SPRING LAKE",
    "STILL RIVER",
    "STONE GATE",
    "STRAITS TURNPIKE",
    "STUB HOLLOW",
    "SULLIVAN FARM",
    "SUNNY VALLEY",
    "SWIMMING HOLE",
    "TANNER HILL",
    "TANNERY BROOK",
    "TECHNOLOGY PARK",
    "TODD HILL",
    "TORRINGFORD WEST",
    "TOWER HILL",
    "TOWN FARM",
    "TOWN HALL",
    "TOWN HILL",
    "TOWN LINE",
    "WAGON WHEEL",
    "WAKE ROBIN",
    "WAKEMAN HILL",
    "WALLENS HILL",
    "WELLERS BRIDGE",
    "WELLS HILL",
    "WEST GRANBY",
    "WEST HILL",
    "WEST LAKE",
    "WEST PEARL",
    "WHISKER HOLLOW",
    "WHITCOMB HILL",
    "WHITE OAK",
    "WHITE PINE",
    "WILDCAT HILL",
    "WILLOW BROOK",
    "WILSON POND",
    "WISHING WILL",
    "WORDEN POINT"
  };
}
