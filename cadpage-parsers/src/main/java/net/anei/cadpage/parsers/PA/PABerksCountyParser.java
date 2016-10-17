package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PABerksCountyParser extends FieldProgramParser {
  
  public PABerksCountyParser() {
    super(OOC_CITY_CODES, "BERKS COUNTY", "PA",
          "UNITCALL! ADDR/iSXa! PLACE! X! CITY INFO DATETIME");
    setupCities(OOC_CITY_LIST);
    removeWords("STREET");
  }
  
  @Override
  public String getFilter() {
    return "@berks.alertpa.org,@c-msg.net,@rsix.roamsecure.net,1410,12101,411912,777,99538";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Strip off message trailer(s)
    body = stripFieldStart(body, "Berks County DES:");
    int pt = body.indexOf("\n\nSent ");
    if (pt < 0) pt = body.indexOf("\nReply ");
    if (pt >= 0) body = body.substring(0,pt).trim();
    boolean force = body.startsWith("<!DOCTYPE");
    if (force) {
      body = cleanDocHeaders(body);
      if (body == null) return false;
    }
    body = stripFieldEnd(body, "=");
    
    // There used to be a Muni: field label.  Which we remove from old  messages
    body = body.replace("; Muni:",        ";");
    
    if (parseFields(body.split(";"), data)) {
      data.strCity = stripFieldEnd(data.strCity, " BORO");
      return true;
    }
    if (!force) return false;
    setFieldList("INFO");
    data.parseGeneralAlert(this, body);
    return true;
  }
  
  
  private static final Pattern CLEAN_HTML_PTN = Pattern.compile("</?(?:span|p)\\b[^>]*>", Pattern.CASE_INSENSITIVE);
  
  private String cleanDocHeaders(String body) {
    int ifCnt = 0;
    for (String line : body.split("\n")) {
      if (line.trim().length() == 0) continue;
      if (line.contains("<!--[if ")) ifCnt++;
      else if (line.contains("<![endif]")) ifCnt--;
      else if (ifCnt == 0) return CLEAN_HTML_PTN.matcher(line).replaceAll("").trim();
    }
    return null;
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("UNITCALL")) return new MyUnitCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern UNIT_CALL_PTN = Pattern.compile("Unit:([-A-Za-z0-9]+) Status:(?:Dispatched|Enroute|En Route|Notify|Arrived On Location|(Stand By)) (.*)");
  private class MyUnitCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = match.group(1);
        String qual = match.group(2);
        String call = match.group(3).trim();
        String desc = CALL_CODES.getProperty(call);
        if (qual != null) call = qual + " - " + call;
        if (desc != null) call = call + " - " + desc;
        data.strCall = call;
      } else {
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT CALL";
    }
  }
  
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern DIR_SECTOR_PTN = Pattern.compile("\\b(?:[NSEW]|[NS][EW]) SECTOR\\b");
  private static final Pattern HANNOVER_PTN = Pattern.compile("\\bHANNOVER\\b");
  private static final Pattern MILLFORD_PTN = Pattern.compile("\\bMILLFORD\\b");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<=UPPER|LOWER|NEW)(?=HANOVER|MACUNGIE|MILFORD|POTTSGROVE)");
  private static final Pattern ADDR_DELIM_PTN = Pattern.compile("(?<!1)/|/(?!2)|=");
  private static final Pattern ADDR_RESD_PTN = Pattern.compile("(.*) (?:RESD?|RESIDENCE)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      field = DIR_SECTOR_PTN.matcher(field).replaceAll("").trim();
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      field = stripFieldEnd(field, "/");
      field = stripFieldStart(field, "ON ");
      
      // Fix misspelled city names
      field = HANNOVER_PTN.matcher(field).replaceAll("HANOVER");
      field = MILLFORD_PTN.matcher(field).replaceAll("MILFORD");
      field = MISSING_BLANK_PTN.matcher(field).replaceAll(" ");
      
      boolean addIntersect = false;
      for (String part : ADDR_DELIM_PTN.split(field)) {
        
        part = part.trim();
        if (part.length() == 0) continue;
        
        if (data.strAddress.length() == 0) {
          int flags = FLAG_IMPLIED_INTERSECT | FLAG_CHECK_STATUS | FLAG_RECHECK_APT;
          if (getRelativeField(+3).length() > 0) flags |= FLAG_NO_CITY;
          parseAddress(StartType.START_ADDR, flags, part, data);
          String left = getLeft();
          Matcher match = ADDR_RESD_PTN.matcher(left);
          if (match.matches()) {
            data.strName = match.group(1).trim();
          } else if (isNotExtraApt(left)) {
            data.strAddress = append(data.strAddress, " ", left);
          } else {
            data.strApt = append(data.strApt, " ", left);
          }
          addIntersect = (getStatus() == STATUS_STREET_NAME || getStatus() == STATUS_NOTHING);
          continue;
        }
        
        // See if this is a city, or a city/county combination
        Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, part);
        String city = res.getCity();
        if (city.length() > 0) {
          String left = res.getLeft();
          if (left.length() == 0 || isCity(left)) {
            if (data.strCity.length() == 0 || data.strCity.endsWith(" CO")) data.strCity = city;
            continue;
          }
        }

        // Is this a named residence
        Matcher match = ADDR_RESD_PTN.matcher(part);
        if (match.matches()) {
          data.strName = match.group(1).trim();
          continue;
        }
        
        if (addIntersect) {
          addIntersect = false;
          String saveAddr = data.strAddress;
          data.strAddress = "";
          parseAddress(StartType.START_ADDR, part, data);
          data.strAddress = append(saveAddr, " & ", data.strAddress);
          String left = getLeft();
          if (isNotExtraApt(left)) {
            data.strAddress = append(data.strAddress, " ", left);
          } else {
            data.strPlace = getLeft();
          }
          continue;
        }
        
        if (isValidAddress(part)) {
          data.strCross = append(data.strCross, " / ", part);
          continue;
        }
        
        data.strPlace = append(data.strPlace, " - ", part);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE? X? NAME";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equalsIgnoreCase(data.strPlace)) return;
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (!DATE_TIME_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = AREA_OF_PTN.matcher(addr).replaceAll("");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern AREA_OF_PTN = Pattern.compile("\\bAREA OF\\b", Pattern.CASE_INSENSITIVE);
  
  // Only out of county cities are included
  // In county cities have their own dedicated field
  private static final String[] OOC_CITY_LIST = new String[]{
      "CHESTER",
      "CHESTER CO",
      "CHESTER COUNTY",
      
        "ELVERSON",
        "ELVERSON BORO",
        "HONEY BROOK",
        "HONEY BROOK BORO",
        
        "HONEY BROOK TWP",
        "N COVENTRY",
        "N COVENTRY TWP",
        "NORTH COVENTRY",
        "NORTH COVENTRY TWP",
        "WARWICK",
        "WARWICK TWP",
        "W NANTMEAL",
        "W NANTMEAL TWP",
        "WEST NANTMEAL",
        "WEST NANTMEAL TWP",
      
      "LANCASTER",
      "LANCASTER CO",
      "LANCASTER COUNTY",
      
        "ADAMSTOWN",
        "ADAMSTOWN BORO",
        "DENVER",
        "DENVER BORO",
        
        "REAMSTOWN",
        
        "BRECKNOCK",
        "BRECKNOCK TWP",
        "CAERNARVON",
        "CAERNARVON TWP",
        "E COCALICO",
        "E COCALICO TWP",
        "EAST COCALICO",
        "EAST COCALICO TWP",
        "W COCALICO",
        "W COCALICO TWP",
        "WEST COCALICO",
        "WEST CACALICO TWP",
      
      "LEBANON",
      "LEBANON CO",
      "LEBANON COUNTY",
      
        "MYERSTOWN",
        "MYERSTOWN BORO",
        
        "FREDERICKSBURG",
        "NEWMANSTOWN",
      
        "BETHEL",
        "BETHEL TWP",
        "JACKSON",
        "JACKSON TWP",
        "MILL CREEK",
        "MILL CREEK TWP",
        "MILLCREEK",
        "MILLCREEK TWP",
        
      "LEHIGH",
      "LEHIGH CO",
      "LEHIGH COUNTY",
      
        "ALBURTIS",
        "ALBURTIS BORO",
        "ANCIENT OAKS",
        "MACUNGIE BORO",

        "MACUNGIE",

        "LOWER MACUNGIE",
        "LOWER MACUNGIE TWP",
        "LOWER MILFORD",
        "LOWER MILFORD TWP",
        "LYNN",
        "LYNN TWP",
        "UPPER MACUNGIE",
        "UPPER MACUNGIE TWP",
        "UPPER MILFORD",
        "UPPER MILFORD TWP",
        "WEISENBERG",
        "WEISENBERG TWP",
        
      "MONTGOMERY",
      "MONTGOMERY CO",
      "MONTGOMERY COUNTY",
      
        "E GREENVILLE",
        "E GREENVILLE BORO",
        "EAST GREENVILLE",
        "EAST GREENVILLE BORO",
        "PENNSBURG",
        "PENNSBURG BORO",
        "POTTSTOWN",
        "POTTSTOWN BORO",
        "RED HILL",
        "RED HILL BORO",

        "POTTSGROVE",
        "SANATOGA",
        "STOWE",

        "DOUGLASS",
        "DOUGLASS TWP",
        "NEW HANOVER",
        "NEW HANOVER TWP",
        "UPPER HANOVER",
        "UPPER HANOVER TWP",
        "UPPER POTTSGROVE",
        "UPPER POTTSGROVE TWP",
        "W POTTSGROVE",
        "W POTTSGROVE TWP",
        "WEST POTTSGROVE",
        "WEST POTTSGROVE TWP",
        
      "SCHUYLKILL",
      "SCHUYLKILL CO",
      "SCHUYLKILL COUNTY",
      
        "AUBURN",
        "AUBURN BORO",
        "DEER LAKE",
        "DEER LAKE BORO",
        "LANDINGVILLE",
        "LANDINGVILLE BORO",
        "NEW RINGGOLD",
        "NEW RINGGOLD BORO",
        "PORT CLINTON",
        "PORT CLINTON BORO",

        "FRIEDENSBURG",
        "LAKE WYNONAH",
        "MCKEANSBURG",
        "RAVINE",
        "SUMMIT STATION",

        "S MANHEIM",
        "S MANHEIM TWP",
        "SOUTH MANHEIM",
        "SOUTH MANHEIM TWP",
        "E BRUNSWICK",
        "E BRUNSWICK TWP",
        "EAST BRUNSWICK",
        "EAST BRUNSWICK TWP",
        "PINE GROVE",
        "PINE GROVE TWP",
        "WASHINGTON",
        "WASHINGTON TWP",
        "WAYNE",
        "WAYNE TWP",
        "W BRUNSWICK",
        "W BRUNSWICK TWP",
        "WEST BRUNSWICK",
        "WEST BRUNSWICK TWP",
        "W PENN",
        "W PENN TWP",
        "WEST PENN",
        "WEST PENN TWP"
    
  };
  
  private static final Properties OOC_CITY_CODES = buildCodeTable(new String[]{
      "LAN CO",    "LANCASTER CO", 
      "LANCO",     "LANCASTER CO",
      "LEB CO",    "LEBANON CO",
      "LEBCO",     "LEBANON CO",
      "MON CO",    "MONTGOMERY CO",
      "MONCO",     "MONTGOMERHY CO",
      "MONT CO",   "MONTGOMERY CO",
      "MONTCO",    "MONTGOMERY CO",
      "MONTGO",    "MONTGOMERY CO",
      "SCH CO",    "SCHUYLKILL CO",
      "SCHCO",     "SCHUYLKILL CO"
  });
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "AFA",      "Fire Alarm",
      "BF",       "Brush Fire",
      "CMA",      "Carbon Monoxide",
      "FS",       "Fire Service",
      "FSB",      "Fire Scene Standby",
      "MF",       "Misc",
      "MVAENT",   "Accident w/ entrapment",
      "MVAUNK",   "Accident unknown inj",
      "MVAWITH",  "Accident w/ injury",
      "RAFA",     "Reading Fire Alarm",
      "RBF",      "Reading Brush Fire",
      "REMERG",   "Reading Emerge",
      "RMISC",    "Reading Misc",
      "RSF",      "Reading StructureFire",
      "SF",       "Structure Fire",
      "VF",       "Vehicle Fire"
  });
  
}
