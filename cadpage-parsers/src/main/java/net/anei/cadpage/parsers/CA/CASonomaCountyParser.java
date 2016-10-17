package net.anei.cadpage.parsers.CA;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Sonoma County, CA
 */
public class CASonomaCountyParser extends FieldProgramParser {
  
  private String version;
  
  public CASonomaCountyParser() {
    super(CITY_LIST, 
           "SONOMA COUNTY", "CA",
           "( SELECT/1 Location:ADDR/S? EID:ID! TYPE_CODE:CALL? CALLER_NAME:NAME CALLER_ADDR:ADDR2/S TIME:TIME Comments:INFO | " +
             "Loc:ADDR? BOX:BOX! TYP:CALL? CN:NAME! C#:PHONE TYP:CALL? TYPE_CODE:SKIP CALLER_NAME:NAME CALLER_ADDR:ADDR2/S TIME:TIME COM:INFO )");
  }
  
  @Override
  public String getFilter() {
    return "sclec@sonoma-county.org,bc71@srcity.org,ps-cst@Sonoma-county.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  private Set<String> unitSet = new HashSet<String>();

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    unitSet.clear();
    if (body.startsWith("LOC:")) body = "Loc:" + body.substring(4);
    body = body.replace(" CN:COM ", " CN: COM:").replace(" CN:COM:", " CN: COM:").replace("TYP:", " TYP:").replace("TIME:", " TIME:");
    version = body.startsWith("Location:") || body.startsWith("EID") ? "1" : "2";
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.startsWith("ID #") && data.strGPSLoc.length() > 0) {
      data.strAddress = data.strAddress + " (" + data.strGPSLoc + ")";
    }
    return true;
  }
  
  @Override
  protected String getSelectValue() {
    return version;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern COLON_MARKER = Pattern.compile(" *: *");
  private static final Pattern SRC_CODE_PTN = Pattern.compile("[A-Z0-9]{0,3}");
  private static final Pattern UNIT_PTN = Pattern.compile("@([A-Z]+\\d+)(?:[- ,]+(.*))?");
  private static final Pattern UNIT2_PTN = Pattern.compile("@([A-Z]+\\d+)");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String fld, Data data) {
      
      // Onstar reports go strait into the address field
      // we will append the GPS coordinates later
      if (fld.startsWith("ID #")) {
        data.strAddress = fld;
        return;
      }
      
      // Split everything by colon markers
      String[] parts = COLON_MARKER.split(fld);
      int ndx = 0;
      
      // Does the first part look like a source code?
      if (ndx < parts.length) {
        String src = parts[ndx];
        if (SRC_CODE_PTN.matcher(src).matches()) {
          data.strSource = src;
          ndx++;
        }
      }
      
      // First part may be (or contain) a unit designation
      if (ndx < parts.length) {
        String unit = parts[ndx];
        Matcher match = UNIT_PTN.matcher(unit);
        if (match.matches()) {
          unit = match.group(1);
          String addr = match.group(2);
          addUnit(unit, data);
          if (addr != null) {
            parts[ndx] = addr;
          } else {
            ndx++;
          }
        }
      }
      
      // Followed by address, which will be parsed later
      fld = (ndx < parts.length ? stripFieldStart(parts[ndx++], "@") : "");
      
      // first part is the address proper
      // anything following that will either be a city or place
      while (ndx < parts.length) {
        String tmp = parts[ndx++];
        if (tmp.equals("@UNIT AT")) continue;
        Matcher match = UNIT2_PTN.matcher(tmp);
        if (match.matches()) {
          addUnit(match.group(1), data);
        }
        else if (data.strCity.length() == 0 && isCity(tmp)) {
          data.strCity = tmp;
        } else {
          tmp = stripFieldStart(tmp, "@");
          data.strPlace = append(data.strPlace, " - ", tmp);
        }
      }

      // Strip off trailing apt
      String apt = "";
      int pt = fld.lastIndexOf(',');
      if (pt >= 0) {
        apt = fld.substring(pt+1).trim();
        fld = fld.substring(0, pt);
        if (data.strCity.length() == 0) {
          parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, apt, data);
          apt = stripFieldEnd(getStart(), "-"); 
        }
      }
      
      // Strip off trailing place name
      pt = fld.lastIndexOf('@');
      if (pt >= 0) {
        data.strPlace = append(fld.substring(pt+1).trim(), " - ", data.strPlace);
        fld = fld.substring(0,pt).trim();
      }
      
      
      fld = fld.replace(" AT ", " & ");
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, fld, data);
      if (data.strCity.length() > 0) {
        if (data.strSource.length() == 0) data.strSource = data.strCity;
        data.strCity = convertCodes(data.strCity, CITY_CODES);
      } else if (data.strSource.length() > 0) {
        data.strCity = convertCodes(data.strSource, CITY_CODES);
      }
      if (data.strCity.length() <= 3) data.strCity = "";
      
      data.strAddress = stripFieldEnd(data.strAddress, "-");
      data.strApt = stripFieldEnd(data.strApt, "-");
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT X CITY SRC PLACE UNIT";
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, " CMCST");
      super.parse(field, data);
    }
  }
  
  private class MyAddress2Field extends MyAddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (data.strAddress.length() > 0) return;
      super.parse(field, data);
    }
  }
  
  private class MyTimeField extends TimeField {
    @Override 
    public void parse(String field, Data data) {
      if (field.length() < 5) return;
      if (field.length() < 8) field = field.substring(0,5);
      super.parse(field, data);
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("^(?:N )?(-\\d{3}.\\d{4,}) (?:T )?(\\d{2}.\\d{4,})(?: METERS \\d+)? *");
  private static final Pattern JUNK_PTN = Pattern.compile(" *(?:Unit ([A-Z0-9]+) (?:requested case number [A-Z0-9]+|.*)|\\*\\* Case number (?:[A-Z0-9]+ has been assigned for [:A-Z0-9]+|.*)|\\*\\* >>>> (?:by: [A-Z ]+ on terminal: [a-z0-9]+|.*)) *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = GPS_PTN.matcher(field);
      if (match.find()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        field = field.substring(match.end());
      }
      
      match = JUNK_PTN.matcher(field);
      if (match.find()) {
        int  last = 0;
        String result = "";
        do {
          result = append(result, " - ", field.substring(last,match.start()));
          last = match.end();
          String unit = match.group(1);
          if (unit !=  null) addUnit(unit, data);
        } while (match.find());
        result = append(result, " - ", field.substring(last));
        field = result;
      }
      
      int pt = field.indexOf("**");
      if (pt >= 0) {
        String tail = field.substring(pt);
        if ("** Case number ".startsWith(tail) || "** >>>> ".startsWith(tail)) {
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS INFO UNIT";
    }
  }
  
  private void addUnit(String unit, Data data) {
    unit = unit.toUpperCase();
    if (unitSet.add(unit)) data.strUnit = append(data.strUnit, " ", unit);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AGU", "AQUA CALIENTE",
      "ANP", "ANNAPOLIS",
      "AST", "CLOVERDALE",
      "BBY", "BODEGA BAY",
      "BEL", "SANTA ROSA",
      "BEN", "SANTA ROSA",
      "BLO", "PETALUMA",
      "BOD", "BODEGA",
      "BOY", "BOYES HOT SPRINGS",
      "CAM", "CAMP MEEKER",
      "CAZ", "CAZADERO",
      "CL",  "CLOVERDALE",
      "CLO", "CLOVERDALE",
      "CO",  "COTATI",
      "COT", "COTATI",
      "DRC", "HEALDSBURG",
      "DUN", "DUNCANS MILLS",
      "ELD", "ELDRIDGE",
      "ELV", "EL VERANO",
      "FIT", "HEALDSBURG",
      "FOR", "FORESTVILLE",
      "FTR", "FORT ROSS",
      "FUL", "SANTA ROSA",
      "GEY", "GEYSERVILLE",
      "GLE", "GLEN ELLEN",
      "GNVL","GUERNEVILLE",
      "GRA", "GRATON",
      "GUE", "GUERNVILLE",
      "HBG", "HEALDSBURG",
      "HE",  "HEALDSBURG",
      "HEA", "HEALDSBURG",
      "HES", "SEBASTOPOL",
      "JCD", "JCD",
      "JEN", "JENNER",
      "KEN", "KENWOOD",
      "KNI", "CALISTOGA",
      "KOR", "FORESTVILLE",
      "LAK", "PETALUMA",
      "LAR", "SANTA ROSA",
      "LKC", "LAKE COUNTY",
      "LSO", "GEYSERVILLE",
      "MAY", "GLEN ELLEN",
      "MEN", "MENDOCINO COUNTY",
      "MRN", "MARIN COUNTY",
      "MRO", "MONTE RIO",
      "MTN", "CALISTOGA",
      "MWS", "SANTA ROSA",
      "NAP", "NAPA",
      "NOV", "NOVATO",
      "OCC", "OCCIDENTAL",
      "PE",  "PETALUMA",
      "PEN", "PENNGROVE",
      "PET", "PETALUMA",
      "RIN", "SANTA ROSA",
      "RLN", "HEALDSBURG",
      "RND", "RIO NIDO",
      "ROH", "ROHNERT PARK",
      "ROS", "SANTA ROSA",
      "RP",  "ROHNERT PARK",
      "RS",  "ROSELAND",
      "SCH", "SCHELLVILLE",
      "SE",  "SEBATOPOL",
      "SEB", "SEBATOPOL",
      "SNMA","SONOMA", 
      "SO",  "SONOMA",
      "SOL", "SOLANO COUNTY",
      "SON", "SONOMA",
      "SR",  "SANTA ROSA",
      "SRO", "SANTA ROSA",
      "SSU", "ROHNERT PARK",
      "TCG", "PETALUMA",
      "TIM", "TIMBER COVE",
      "TSR", "SEA RANCH",
      "TWI", "SEBATOPOL",
      "TWR", "PETALUMA",
      "VFR", "VALLEY FORD",
      "VJO", "VALLEJO",
      "WI",  "WINDSOR",
      "WIN", "WINDSOR",
      "WSR", "SANTA ROSA",
      
      "ANCHOR BAY",   "ANCHOR BAY",
      "CLSTGA",       "CALISTOGA",
      "GEYSERVILLE",  "GEYSERVILLE",
      "GUALALA",      "GUALALA",
      "MANCHESTER",   "MANCHESTER",
      "POINT ARENA",  "POINT ARENA",
      "PT ARENA",     "PT ARENA",
      "PT AREANA",    "PT ARENA",
      "SEA RANCH",    "SEA RANCH",
      "SBSTPL",       "SEBATOPOL",
      "TOMALAES",     "TOMALES",
      "TOMALES",      "TOMALES",

      "BDGA",      "BODEGA",
      "BDGA BAY",  "BODEGA BAY"
  });
  
  private static final String[] CITY_LIST = CITY_CODES.keySet().toArray(new String[CITY_CODES.size()]);
}
