package net.anei.cadpage.parsers.CT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class CTWindhamCountyAParser extends SmartAddressParser {

  public CTWindhamCountyAParser() {
    this("WINDHAM COUNTY", "CT");
  }

  CTWindhamCountyAParser(String defCity, String defState) {
    super(defCity, defState);
    setFieldList("SRC UNIT CH PRI INFO CALL ADDR PLACE APT CITY ST X TIME");
    setupMultiWordStreets(MWORD_STREET_LIST);
    addRoadSuffixTerms("DRIVE");
    removeWords("BUS");
  }
  
  @Override
  public String getFilter() {
    return "qvecpaging@qvec.org,messaging@iamresponding.com";
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("([ A-Za-z0-9]+?)  +");
  private static final Pattern CHANNEL_PTN = Pattern.compile("(UHF-\\d|\\d\\d\\.\\d\\d|\\d{3}|\\b(?:EKONK|KILLINGLY|UNION|THOMPSON) \\d{3}|HIGH-BAND) +");
  private static final Pattern PRIORITY_PTN = Pattern.compile("^PRI +(\\d) +");
  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d:\\d\\d");
  private static final Pattern ADDR_DEL_PTN = Pattern.compile(" \\* |\n");
  private static final Pattern RESERVE_CALL_PTN = Pattern.compile(".*(?:CALL FROM|ALERT|ALARM|APPLIANCE FIRE|FALL INJURY|INJURED PERSON|LIFT ASSIST|VEHICLE ACCIDENT)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|APARTMENT|ROOM|RM|UNIT|LOT)[- ]*(.*)|\\d{1,5}|[A-Z]|[A-Z]-?\\d{1,5}|.* (?:FLR|FLOOR)|WING .*", Pattern.CASE_INSENSITIVE);
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() != 0) {
      subject = stripFieldEnd(subject, " Page");
      data.strSource = subject;
    }
    
    Matcher  match = UNIT_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strUnit = match.group(1);
      body = body.substring(match.end());
    }
    
    match = CHANNEL_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strChannel = match.group(1);
      body = body.substring(match.end());
    }
    
    match = PRIORITY_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strPriority = match.group(1);
      body = body.substring(match.end()).trim();
    }
    
    // Everything is option, but we had it find **SOMETHING** we recognize
    if (data.strUnit.length() == 0 && data.strChannel.length() == 0 && data.strPriority.length() == 0) return false;
    
    Parser p = new Parser(body);
    String sAddr = p.get("(X-STS ");
    data.strCross = p.get(')');
    String sTime = p.get();
    if (TIME_PTN.matcher(sTime).matches()) data.strTime = sTime;
    
    // New format > call * address * place city
    // alternate format call \n address \n place city
    // old format > call address / place city
    sAddr = stripFieldStart(sAddr, "/");
    String sPlaceCity;
    String[] flds = ADDR_DEL_PTN.split(sAddr);
    if (flds.length == 3 || flds.length == 4) {
      int pt = 0;
      if (flds.length == 4) data.strSupp = flds[pt++].trim();
      data.strCall = flds[pt++].trim();
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, flds[pt++].trim(), data);
      sPlaceCity = flds[pt++].trim();
    }
    
    else {
      int pt = sAddr.lastIndexOf(" / ");
      if (pt < 0) return false;
      sPlaceCity = sAddr.substring(pt+3).trim();
      sAddr = sAddr.substring(0,pt).trim();
      
      // There has been a problem with some call descriptions that contains things that look like
      // an address, so we try to identify and parser those out.
      String reserveCall = "";
      int flags = FLAG_START_FLD_REQ;
      match = RESERVE_CALL_PTN.matcher(sAddr);
      if (match.lookingAt()) {
        reserveCall = match.group();
        sAddr = sAddr.substring(match.end()).trim();
        flags = 0;
      }
      parseAddress(StartType.START_CALL, flags | FLAG_IGNORE_AT | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, sAddr, data);
      if (reserveCall.length() > 0 && data.strAddress.length() == 0) {
        parseAddress(data.strCall, data);
        data.strCall = "";
      }
      data.strCall = append(reserveCall, " ", data.strCall);
    }
    data.strCall = data.strCall.replaceAll("  +", " ");
    
    String city = CITY_SET.getCode(sPlaceCity.toUpperCase(), true);
    if (city != null) {
      int pt = sPlaceCity.length()-city.length();
      data.strCity = sPlaceCity.substring(pt);
      sPlaceCity = sPlaceCity.substring(0,pt).trim();
    }
    if (sPlaceCity.length() > 0) {
      
      // Intersections sometimes bleed into the place name :(
      if (checkAddress(data.strAddress) == STATUS_STREET_NAME) {
        Result res = parseAddress(StartType.START_ADDR, sPlaceCity);
        if (res.getStatus() == STATUS_STREET_NAME) {
          String tmp = data.strAddress;
          data.strAddress = "";
          res.getData(data);
          data.strAddress = append(tmp, " & ", data.strAddress);
          sPlaceCity = res.getLeft();
        }
      }
      match = APT_PTN.matcher(sPlaceCity);
      if (match.matches()) {
        String tmp = match.group(1);
        if (tmp == null) tmp = sPlaceCity;
        data.strApt = append(data.strApt, "-", tmp);
      } else {
        int pt = sPlaceCity.lastIndexOf('/');
        if (pt >= 0) {
          String tmp = sPlaceCity.substring(pt+1).trim();
          match = APT_PTN.matcher(tmp);
          if (match.matches()) {
            String tmp2 = match.group(1);
            if (tmp2 == null) tmp2 = tmp;
            data.strApt = append(data.strApt, "-", tmp2);
            sPlaceCity = sPlaceCity.substring(0,pt).trim();
          }
        }
        data.strPlace = sPlaceCity;
      }
    }
    
    String st = CITY_ST_TABLE.getProperty(data.strCity.toUpperCase());
    if (st != null) data.strState = st;
    
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "ALL HALLOWS",
    "ALLEN HILL",
    "ASHFORD CENTER",
    "ATTAWAUGAN CROSSING",
    "AUTUMN VIEW",
    "BAILEY HILL",
    "BARBER HILL",
    "BARLOW CEMETERY",
    "BARTLETT MEADOW",
    "BEACH POND",
    "BEAVER DAM",
    "BLACK ROCK",
    "BLACKMER DOWNS",
    "BRAATEN HILL",
    "BRANDY HILL",
    "BRAYMAN HOLLOW",
    "BREAKNECK HILL",
    "BREAULTS LANDING",
    "BRUSH HILL",
    "BUCK HILL",
    "BUCKLEY HILL",
    "BULL HILL",
    "BUNDY HILL",
    "BUNGAY HILL",
    "CAMP YANKEE",
    "CAT HOLLOW",
    "CENTER CEMETERY",
    "CENTER SCHOOL",
    "CENTRE PIKE",
    "CHANDLER SCHOOL",
    "CHASE HILL",
    "CHENEY MILL",
    "CHERRY TREE CORNER",
    "CHESTNUT HILL",
    "CHILD DOME",
    "CHRISTIAN HILL",
    "CLEAR VIEW",
    "CLUB HOUSE",
    "COATNEY HILL",
    "CONNECTICUT MILLS",
    "COOK HILL",
    "COTTON BRIDGE",
    "COUNTRY CLUB",
    "COUNTY HOME",
    "CRYSTAL POND",
    "DEEP RIVER",
    "DEER MEADOW",
    "DEWING SCHOOL",
    "DOCTOR PIKE",
    "DOG HILL",
    "DR FOOTE",
    "DUGG HILL",
    "EKONK HILL",
    "ELMWOOD HILL",
    "ENGLISH NEIGHBORHOOD",
    "FABYAN WOODSTOCK",
    "FALLS BASHAN",
    "FALLS BASHIN",
    "FIRE TOWER",
    "FOUR SEASONS",
    "GAY HEAD",
    "GENERAL LYON",
    "GRAND VIEW",
    "GREEN ACRES",
    "GREEN HOLLOW",
    "GROVE ST STERLING HILL",
    "HALLS HILL",
    "HALLS POND",
    "HIGHLAND FARMS",
    "ICE HOUSE",
    "IDE PERRIN",
    "INDIAN INN",
    "INDIAN RUN",
    "INDIAN SPRINGS",
    "JOHN PERRY",
    "KENNERSON RESERVOIR",
    "KILLINGLY COMMONS",
    "KIMBALL HEIGHTS",
    "KINSMAN HILL",
    "LAKE HAYWARD",
    "LAKE VIEW",
    "LAUREL HILL",
    "LAUREL POINT",
    "LEBANON HILL",
    "LITTLE BUNGEE HILL",
    "LITTLE HORN",
    "LONG POND",
    "LOUISA VIENS",
    "LOWELL DAVIS",
    "MARGARET HENRY",
    "MASON HILL",
    "MILL BRIDGE",
    "MOOSUP POND",
    "OLDE MEADOW",
    "OLEAROS HILL",
    "OWL NEST",
    "PAINE DISTRICT",
    "PARENT HILL",
    "PINE CREST",
    "PINE HOLLOW",
    "PLEASANT VIEW",
    "POLE BRIDGE",
    "POND FACTORY",
    "PORTER PLAIN",
    "PRESTON ALLEN",
    "PROVIDENCE PIKE BAILEY HILL",
    "PULPIT ROCK",
    "QUADDICK MOUNTAIN",
    "QUADDICK TOWN FARM",
    "RATHBUN HILL",
    "RED BRIDGE",
    "REDHEAD HILL",
    "RIFLE RANGE",
    "RILEY CHASE",
    "RIVER WALK",
    "ROCKY HILL",
    "ROCKY HOLLOW",
    "ROSELAND PARK",
    "ROSS HILL",
    "ROUND HILL",
    "SAND DAM",
    "SAND HILL",
    "SAW MILL HILL",
    "SCHOOL HOUSE HILL",
    "SCHOOL HOUSE",
    "SHAILOR HILL",
    "SHEPARD HILL",
    "SNAKE MEADOW",
    "SPRAGUE HILL",
    "SPRING HILL",
    "SQUAW ROCK",
    "STERLING HILL",
    "SUMNER HILL",
    "SUNSET HILL",
    "TAFT POND",
    "THOMPSON HILL",
    "TOWN FARM",
    "TOWN HOUSE",
    "TPKE WALKER",
    "TUCKER DISTRICT",
    "TUFT HILL",
    "TUNNEL HILL",
    "VALLEY VIEW",
    "WEST COVE",
    "WETHERELL HILL",
    "WHIP POOR WILL",
    "YOSEMITE VALLEY"
  };
  
  private static final ReverseCodeSet CITY_SET = new ReverseCodeSet(
      "ASHFORD",
      "BROOKLYN",
         "EAST BROOKLYN",
      "CANTERBURY",
      "CHAPLIN",
      "EASTFORD",
      "HAMPTON",
      "KILLINGLY",
         "DANIELSON",
      "PLAINFIELD",
         "CENTRAL VILLAGE",
         "MOOSUP",
         "PLAINFIELD VILLAGE",
         "WAUREGAN",
      "POMFRET",
      "PUTNAM",
         "PUTNAM DISTRICT",
      "SCOTLAND",
      "STERLING",
         "ONECO",
      "THOMPSON",
         "NORTH GROSVENOR DALE",
         "QUINEBAUG",
      "WINDHAM",
         "SOUTH WINDHAM",
         "WILLIMANTIC",
      "WOODSTOCK",
         "SOUTH WOODSTOCK",
      
      // New London County
      "NEW LONDON",
      "NORWICH",

      "BOZRAH",
      "COLCHESTER",
          "WESTCHESTER",
      "EAST LYME",
          "FLANDERS",
          "NIANTIC",
      "FRANKLIN",
      "GRISWOLD",
          "JEWETT CITY",
          "HOPEVILLE",
          "GLASGO",
          "PACHAUG",
      "GROTON",
          "GROTON LONG POINT",
          "LONG HILL",
          "MYSTIC",
          "NOANK",
          "POQUONOCK BRIDGE",
      "LEBANON",
      "LEDYARD",
          "GALES FERRY",
          "LEDYARD CENTER",
      "LISBON",
      "LYME",
      "MONTVILLE",
          "CHESTERFIELD",
          "MOHEGAN",
          "OAKDALE",
          "OXOBOXO RIVER",
          "UNCASVILLE",
      "NORTH STONINGTON",
      "OLD LYME",
      "PRESTON",
          "POQUETANUCK",
          "PRESTON CITY",
      "SALEM",
      "SPRAGUE",
          "BALTIC",
      "STONINGTON",
          "PAWCATUCK",
          "MYSTIC",
          "OLD MYSTIC",
      "VOLUNTOWN",
      "WATERFORD",
          "QUAKER HILL",
          
      // Middlesex County
      "EAST HADDAM",
      "E HADDAM",
          "MOODUS",
      
      // Tolland County
      "HEBRON",
          
      // Worcester County, MA
      "WEBSTER"
  );
  
  private static final Properties CITY_ST_TABLE = buildCodeTable(new String[]{
      "WEBSTER",    "MA"
  });
}
