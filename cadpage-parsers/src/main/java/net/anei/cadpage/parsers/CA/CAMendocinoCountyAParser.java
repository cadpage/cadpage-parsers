package net.anei.cadpage.parsers.CA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Mendocino County, CA (A)
 */
public class CAMendocinoCountyAParser extends FieldProgramParser {
  
  public CAMendocinoCountyAParser() {
    super(CITY_CODES, "MENDOCINO COUNTY", "CA",
           "( SELECT/RR ID CALL ADDR UNIT INFO! INFO/N+ " +
           "| SELECT/2 CALL ADDR Cross-Street:X! Remarks:INFO! INFO/N+? GPS! Resources:UNIT! Cmd:CH! Tac:CH/L " + 
           "| CALL ADDR! ( IDGPSUNIT! INFO/N+ |  ( ID INFO/N+ RA:MAP! GPS! UNIT! | INFO/N+ Map:MAP! GPS! INFO/N+? ( UNITID! | UNIT ID ) ) Cmd:CH! Tac:CH/L! Air:CH/L? INFO/N+? ( DATETIME | MAPURL ) ) )");
  }
  
  @Override
  public String getFilter() {
    return "meucad@fire.ca.gov,lnucad@fire.ca.gov,@albionfire.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  private static final Pattern RR_DELIM = Pattern.compile("[;\n]");
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("CLOSE:")) {
      body = body.substring(6).trim();
      data.msgType = MsgType.RUN_REPORT;
      setSelectValue("RR");
      return parseFields(RR_DELIM.split(body), data);
    }
    int semiBrk = body.indexOf(';');
    if (semiBrk < 0) semiBrk = Integer.MAX_VALUE;
    int lineBrk = body.indexOf('\n');
    if (lineBrk < 0) lineBrk = Integer.MAX_VALUE;
    if (lineBrk <= semiBrk) {
      setSelectValue("2");
      return parseFields(body.split("\n"), data);
    } else {
      setSelectValue("1");
      return parseFields(body.split(";"), data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("IDGPSUNIT")) return new MyIdGpsUnitField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("UNITID")) return new MyUnitIdField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("MAPURL")) return new SkipField("http:.*|<a href=.*", true);
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
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES).replace('_', ' ');
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }
  
  private static final Pattern ID_GPS_UNIT_PTN =
    Pattern.compile("Inc# *(\\d*) *X: ([- 0-9.]+) Y: ([- 0-9.]+)(.*)");
  private class MyIdGpsUnitField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ID_GPS_UNIT_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCallId = match.group(1).trim();
      // GPS coordinates have to be reversed, Google expects Latitude first.
      String gpsLoc = match.group(3) + ',' + match.group(2);
      setGPSLoc(gpsLoc, data);
      data.strUnit = match.group(4).trim();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    
    @Override
    public String  getFieldNames() {
      return "ID GPS UNIT";
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("Inc#"))  return false;
      field = field.substring(4).trim();
      data.strCallId = field;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern GPS_PTN1 = Pattern.compile("Y: *(.*?) *X:(.*)");
  private static final Pattern GPS_PTN2 = Pattern.compile("X: *(.*?) *Y:(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN1.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        return true;
      }
      match = GPS_PTN2.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(2)+','+match.group(1), data);
        return true;
      }
      return false;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
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
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d-[A-Z][a-z]{2}-\\d{4})/(\\d\\d:\\d\\d:\\d\\d)");
  private static final DateFormat DATE_FMT = new SimpleDateFormat("dd-MMM-yyyy");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      setDate(DATE_FMT, match.group(1), data);
      data.strTime = match.group(2);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
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
