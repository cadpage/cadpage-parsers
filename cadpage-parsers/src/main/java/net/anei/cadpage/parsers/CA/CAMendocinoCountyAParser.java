package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Mendocino County, CA (A)
 */
public class CAMendocinoCountyAParser extends FieldProgramParser {
  
  public CAMendocinoCountyAParser() {
    super(CITY_CODES, "MENDOCINO COUNTY", "CA",
           "CALL ADDR! ( Inc:IDGPSUNIT! INFO/N+ | INFO/N+ Map:MAP! Y:GPS! INFO/N+? ( UNITID! | UNIT Inc:ID ) Cmd:CH! Tac:CH/L! INFO/N+? MAPURL )");
  }
  
  @Override
  public String getFilter() {
    return "meucad@fire.ca.gov,@albionfire.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" Inc# ", " Inc: ");
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("IDGPSUNIT")) return new IdGpsUnitField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("UNITID")) return new MyUnitIdField();
    if (name.equals("MAPURL")) return new SkipField("http:.*", true);
    return super.getField(name);
  }
  
  private static final Pattern TRAIL_PARENS = Pattern.compile("\\(([^\\(]*?)\\)$");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = TRAIL_PARENS.matcher(field);
      if (match.find()) {
        data.strPlace = match.group(1);
        field = field.substring(0,match.start()).trim();
      }
      Parser p = new Parser(field);
      data.strPlace = append(p.getOptional('@'), " / ", data.strPlace);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR CITY";
    }
  }
  
  private static final Pattern ID_GPS_UNIT_PTN =
    Pattern.compile("(\\d*) *X: ([- 0-9.]+) Y: ([- 0-9.]+)(.*)");
  private class IdGpsUnitField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_GPS_UNIT_PTN.matcher(field);
      if (match.matches()) {
        data.strCallId = match.group(1).trim();
        // GPS coordinates have to be reversed, Google expects Latitude first.
        String gpsLoc = match.group(3) + ',' + match.group(2);
        setGPSLoc(gpsLoc, data);
        data.strUnit = match.group(4).trim();
      }
    }
    
    @Override
    public String  getFieldNames() {
      return "ID GPS UNIT";
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" X: ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_ID_PTN = Pattern.compile("(.*?)Inc# (\\d+)");
  private class MyUnitIdField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_ID_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = match.group(1).trim();
      data.strCallId = match.group(2);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT ID";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALBI",   "ALBION",
      "ANCB",   "ANCHOR BAY",
      "ARENA",  "POINT ARENA",
      "BOON",   "BOONVILLE",
      "BRANSB", "BRANSCOMB",
      "BROOK",  "BROOKTRAILS",
      "CALP",   "UKIAH",
      "CASP",   "MENDOCINO",
      "CLEON",  "FORT BRAGG",
      "COMP",   "COMPTCHE",
      "COVL",   "COVELO",
      "DOSR",   "DOS RIOS",
      "ELK",    "ELK",
      "FORTB",  "FORT BRAGG",
      "GUALA",  "GUALALA",
      "HALESG", "HALES GROVE",
      "HOPL",   "HOPLAND",
      "HOPR",   "HOPLAND",
      "INGL",   "INGLENOOK",
      "IRISH",  "MANCHESTER",
      "LAYT",   "LAYTONVILLE",
      "LEGG",   "LEGGETT",
      "LITR",   "LITTLE RIVER",
      "LONV",   "LONGVALE",
      "MANC",   "MANCHESTER",
      "MCNR",   "HOPLAND",
      "MENDO",  "MENDOCINO",
      "NAVA",   "NAVARRO",
      "OOJ",    "OUT OF JURISDICTION",
      "ORR",    "UKIAH",
      "PHILO",  "PHILO",
      "PIERC",  "PIERCY",
      "PILS",   "LAKE PILLSBURY",
      "POTV",   "POTTER VALLEY",
      "REDV",   "REDWOOD VALLEY",
      "ROCKP",  "ROCKPORT",
      "ROGH",   "UKIAH",
      "SHER",   "WILLITS",
      "TALM",   "UKIAH",
      "TRIN",   "Mendocino National Forest, Mad River, CA",
      "UKH",    "UKIAH",
      "UKV",    "UKIAH",
      "VICHY",  "UKIAH",
      "VNAR",   "POTTER VALLY",
      "WESTP",  "WESTPORT",
      "WHALG",  "WHALE GULCH",
      "WILL",   "WILLITS",
      "YORK",   "YORKVILLE",
      
      "MCNAB_RANCH", "UKIAH"
  });
}
