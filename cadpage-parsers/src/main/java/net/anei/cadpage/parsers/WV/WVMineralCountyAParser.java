package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class WVMineralCountyAParser extends FieldProgramParser {
  
  private static final Pattern BAD_ADDRESS_PTN = Pattern.compile(".*\\bTRAILER COURT\\b.*", Pattern.CASE_INSENSITIVE);
  
  public WVMineralCountyAParser() {
    super(CITY_LIST, "MINERAL COUNTY", "WV",
          "SRCID DATETIME STATUS CALL/SDS ADDR!");
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets("WEST PIEDMONT EXT");
    setupRejectAddressPattern(BAD_ADDRESS_PTN);
  }
  
  @Override
  public String getFilter() {
    return "xdc@mineralcounty911.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] flds = body.split(" >");
    if (flds.length < 5) flds = body.split("\n");
    return parseFields(flds, 5, data);
  }

  private static final Pattern SOURCE_ID_PTN = Pattern.compile("(?:\\?\\?\\? )?([A-Z][A-Z0-9]{2,4})\\.?:(\\d{4}:\\d{4})");
  private class SourceIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = SOURCE_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = match.group(1);
      data.strCallId = match.group(2);
    }
    
    @Override
    public String getFieldNames() {
      return "SRC ID";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRCID")) return new SourceIdField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("STATUS")) return new MyStatusField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern STATUS_PTN = Pattern.compile("ALERTED|(ENROUTE|ON SCENE|RETURN TO QUARTERS|TRANSPORT|STANDBY/STAGE|PENDING|COMPLETED)");
  private class MyStatusField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = STATUS_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = getOptGroup(match.group(1));
    }
  }
  
  private class MyDateTimeField extends DateTimeField {
    public MyDateTimeField() {
      super("\\d{4}-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    }
    @Override
    public void parse(String field, Data data) {
      data.strDate = field.substring(5,7) + '/' + field.substring(8,10) + '/' + field.substring(0,4);
      data.strTime = field.substring(11);
    }
  }
  
  private static final Pattern RT_DOT_PTN = Pattern.compile("\\bRT\\. *", Pattern.CASE_INSENSITIVE);
  private static final Pattern SPEC_CROSS_PTN = Pattern.compile("\\(([^\\(\\)]*);\\)");
  private static final Pattern STATION_PTN = Pattern.compile("(STATION +\\d+)( +.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_DELIM = Pattern.compile(" *[,;] *");
  private static final Pattern INTERSECTION_PTN = Pattern.compile("INTERSECTI?ON (?:OF )? *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|#) *#?([^ ]+)");
  private static final Pattern NEXT_TO_PTN = Pattern.compile("NEXT TO (.*) INTERSECTION", Pattern.CASE_INSENSITIVE);
  private static final Pattern CROSS_PREFIX_PTN = Pattern.compile("(?:ACROSS FROM|FROM|NEAR|OFF|ON|TO) +(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern NO_CROSS_PREFIX_PTN = Pattern.compile("(?:TURN|PASS|PAST) .*", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      if (data.strCall.endsWith("INFORMATION")) {
        data.strPlace = field;
        return;
      }
      
      field = RT_DOT_PTN.matcher(field).replaceAll("RT ");
      
      String cross = "";
      Matcher match = SPEC_CROSS_PTN.matcher(field);
      if (match.find()) {
        cross = match.group(1).trim();
        field = field.substring(0,match.start()) + field.substring(match.end());
      }
      
      match = STATION_PTN.matcher(field);
      if (match.matches()) field = match.group(1) + ',' + match.group(2);
      
      // Split address into component parts
      field = field.replace("(;)", "");
      String[] parts = ADDR_DELIM.split(field);
      
      // Figuring out where the address is can be tricky.
      int addrNdx = -1;
      boolean foundPart = false;
      
      // If there is only one part, it must be the address
      if (parts.length == 1) {
        addrNdx = 0;
        match = INTERSECTION_PTN.matcher(parts[0]);
        if (match.matches()) parts[0] = match.group(1);
      }
      
      // Otherwise, check the next 2 parts to see if they contain anything
      // we would recognize as something that should follow an address;
      else {
        for (int ndx = 1; ndx<= 2; ndx++) {
          if (ndx >= parts.length) break;
          if (processPart(parts[ndx], false, data)) {
            foundPart = true;
            addrNdx = ndx-1;
            break;
          }
        }
      }
      
      // If there is only one suspected address thingie, parse it as such
      int flags = FLAG_IMPLIED_INTERSECT | FLAG_AT_SIGN_ONLY | FLAG_NO_IMPLIED_APT;
      if (data.strCity.length() > 0) flags |= FLAG_NO_CITY;
      if (addrNdx == 0) {
        StartType st = parts.length == 1 ? StartType.START_PLACE : StartType.START_ADDR;
        parseAddress(st, flags, parts[addrNdx], data);
        data.strSupp = append(getLeft(), " / ", data.strSupp);
        if (data.strAddress.length() == 0) {
          parseAddress(data.strPlace, data);
          data.strPlace = "";
        }
        addrNdx = 1;
      }
      
      // No such luck.  We will have to check both the first two parts to see
      // which one looks like a better address;
      else {
        flags |= FLAG_CHECK_STATUS;
        Result res1 = parseAddress(StartType.START_ADDR, flags, parts[0]);
        String tmp2 = parts[1];
        match = CROSS_PREFIX_PTN.matcher(tmp2);
        if (match.matches()) tmp2 = match.group(1);
        
        Result res2 = parseAddress(StartType.START_ADDR, flags, tmp2);
        boolean placeIntersect = res1.getStatus() == STATUS_STREET_NAME && res2.getStatus() == STATUS_STREET_NAME && 
                                 res1.getLeft().length() == 0 && res2.getLeft().length() == 0; 
        if (placeIntersect) {
          res1.getData(data);
          String addr1 = data.strAddress;
          String place1 = data.strPlace;
          data.strAddress = data.strPlace = "";
          res2.getData(data);
          data.strAddress = append(addr1, " & ", data.strAddress);
          data.strPlace = append(place1, " - ", data.strPlace);
          addrNdx = 1;
        }
        
        else {
          addrNdx = 0;
          if (placeIntersect || res2.getStatus() > res1.getStatus()) {
            data.strPlace = parts[0];
            res1 = res2;
            addrNdx = 1;
          } else if (foundPart) {
            data.strPlace = parts[1];
            addrNdx = 1;
          }
          res1.getData(data);
          data.strSupp = append(res1.getLeft(), " / ", data.strSupp);
        }
        if (foundPart) addrNdx++;
      }

      // OK, we got the address and possible the thing after the address
      // not process everything else
      for (int ndx = addrNdx+1; ndx < parts.length; ndx++) {
        processPart(parts[ndx], true, data);
      }
      
      data.strCross = append(data.strCross, " / ", cross);
      
      if (data.strCity.equalsIgnoreCase("MCOOLE")) data.strCity = "MCCOOLE";
      else if (data.strCity.equalsIgnoreCase("FROST BURG")) data.strCity = "FROSTBURG";
      if (data.strCity.equalsIgnoreCase("WESTERNPORT") ||
          data.strCity.equalsIgnoreCase("MCCOOLE") ||
          data.strCity.equalsIgnoreCase("FROSTBURG")) data.strState = "MD";
    }
    
    private boolean processPart(String part, boolean forceInfo, Data data) {
      String upPart = part.toUpperCase();
      if (upPart.equals("MNRL WV")) return true;
      if (upPart.startsWith("BETWEEN ")) {
        data.strCross = append(data.strCross, " / ", part.substring(8).trim());
        return true;
      }
      Matcher match = APT_PTN.matcher(upPart);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        return true;
      }
      match = NEXT_TO_PTN.matcher(part);
      if (match.matches()) {
        data.strAddress = data.strAddress + " & " + match.group(1);
        return true;
      }
      if (data.strCity.length() == 0  && !isValidAddress(part)) {
        Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, part);
        if (res.isValid()) {
          res.getData(data);
          data.strSupp = append(data.strSupp, ", ", res.getLeft());
          return true;
        }
      }
      
      if (!forceInfo) return false;
      
      if (!NO_CROSS_PREFIX_PTN.matcher(part).matches()) {
        String cross = part;
        match = CROSS_PREFIX_PTN.matcher(cross);
        if (match.matches()) cross = match.group(1);
        if (isValidAddress(cross)) {
          data.strCross = append(data.strCross, " / ", cross);
          return true;
        }
      } 
      data.strSupp = append(data.strSupp, ", ", part);
      return true;
      
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY ST X INFO";
    }
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    "BELL BABB",
    "BIBLE CHURCH",
    "BRIDGE HOLLOW",
    "CABIN RUN",
    "CHAZ DEREMER",
    "CUT OFF",
    "DOUBLE CRIBBS",
    "DRY RUN",
    "EAST PIEDMONT",
    "FORT ASHBY",
    "FOXES HOLLOW",
    "FRIED MEAT RIDGE",
    "GOLDEN CROSS",
    "GRAYSON GAP",
    "HERSHEY HOLLOW",
    "HICKORY HILL",
    "HIDDEN VIEW",
    "HOOKER HOLLOW",
    "HOOKER HOLLOW",
    "HORSESHOE RUN",
    "JAKE STAGGERS",
    "MAPLE HOLLOW",
    "MILL RUN",
    "MUD RUN",
    "NETHKIN HILL",
    "NORTH MAIN",
    "ORCHARD MUSE",
    "PARRILL HLLW",
    "PARRILL HOLLOW",
    "PATTERSON CREEK",
    "PATTERSONS CREEK",
    "PATTERSONS CREEK",
    "PHILLIP VINCENT",
    "PIN OAK",
    "RAVEN RIDGE",
    "REESES MILL",
    "ROCKLAND FARM",
    "RV BROWN",
    "SAINT CLOUD",
    "SOUTH CHURCH",
    "SOUTH FOXS HOLLOW",
    "SOUTH MAIN",
    "SOUTH VALLEY VIEW",
    "SOUTH WATER",
    "ST CLOUD",
    "STATE CLOUD",
    "SUNNY BROOK",
    "SUSAN FLEEK",
    "SWEET SERENITY",
    "WEBBS FARM",
    "WEST HAMPSHIRE",
    "WEST PIEDMONT",
    "WHITE WAY",
    "WHITE WILLOW"
  };
  
  private static final String[] CITY_LIST = new String[]{
    "KEYSER",
    "PIEDMONT",
    "RIDGELEY",
    "CARPENDALE",
    "ELK GARDEN",

    "ANTIOCH",
    "ATLANTIC HILL",
    "BARNUM",
    "BERYL",
    "BLAINE",
    "BURLINGTON",
    "CHAMPWOOD",
    "CLAYSVILLE",
    "DANS RUN",
    "EMORYVILLE",
    "FOOTE STATION",
    "FORGE HILL",
    "FORT ASHBY",
    "FOUNTAIN",
    "HAMPSHIRE",
    "HARTMANSVILLE",
    "HEADSVILLE",
    "KEYMONT",
    "LAUREL DALE",
    "LIMESTONE",
    "MARKWOOD",
    "NETHKIN",
    "NEW CREEK",
    "OAKMONT",
    "PATTERSON CREEK",
    "REESES MILL",
    "RIDGELEY",
    "RIDGEVILLE",
    "ROCKET CENTER",
    "RUSSELLDALE",
    "SHORT GAP",
    "SKYLINE",
    "SULPHUR CITY",
    "WAGONER",
    "WILEY FORD",
    
    "FROST BURG",
    "FROSTBURG",
    "MCOOLE",
    "MCCOOLE",
    "WESTERNPORT",
    
    // Grant County
    "MAYSVILLE"
  };
}
