package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class KYHardinCountyBParser extends FieldProgramParser {
  
  public KYHardinCountyBParser() {
    super(CITY_LIST, "HARDIN COUNTY", "KY",
          "CALL CALL+? ADDR/Z! ( CITY ST? X! | X ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "flahertyfire@bbtel.com, todd.vinton@bbtel.com, noreply@meadeky.gov";
  }
  
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = UNIT_PTN.matcher(body);
    if (match.find()) {
      data.strUnit = match.group(1);
      body = body.substring(0,match.start());
    }
    
    // Occasionally the address consists of GPS coordinates which have to be merged into one field
    body = GPS_PTN.matcher(body).replaceAll("$1_$2");
    
    if (!parseFields(body.split(","), 3, data)) return false;
    data.strAddress = data.strAddress.replace('_', ',');
    return true;
  }
  private static final Pattern UNIT_PTN = Pattern.compile(" *\\*\\*([A-Z0-9]+)\\*\\*$");
  private static final Pattern GPS_PTN = Pattern.compile("([-+]?\\d+\\.\\d{3,}) *, *([-+]?\\d+\\.\\d{3,})");
  
  @Override
  public String getProgram() {
    return super.getProgram() + " UNIT";
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, ", ", field);
    }
  }
  
  
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      return super.checkParse(field,  data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("//")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      if (field.startsWith("/")) field = field.substring(1).trim();
      if (field.endsWith("//")) field = field.substring(0, field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_GPS_PTN = Pattern.compile("([-+]?\\d+\\.\\d{3,})_([-+]?\\d+\\.\\d{3,})");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        data.strGPSLoc = field.replace('_', ','); 
      } else {
        data.strSupp = append(data.strSupp, ", ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "GPS INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ST")) return new SkipField("KY(?: +\\d{5})?", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Incorporated Places
    "ELIZABETHTOWN",
    "MULDRAUGH",
    "RADCLIFF",
    "SONORA",
    "UPTON",
    "VINE GROVE",
    "WEST POINT",

    // Census-designated places
    "FORT KNOX",

    // Unincorporated places
    "BIG SPRING",
    "BLUE BALL",
    "CECILIA",
    "COLESBURG",
    "DEVER HOLLOW",
    "EASTVIEW",
    "GLENDALE",
    "HARCOURT",
    "HOWELL SPRING",
    "HARDIN SPRINGS",
    "HOWEVALLEY",
    "MILL CREEK",
    "NEW FRUIT",
    "NOLIN",
    "QUAKER VALLEY",
    "RED MILLS",
    "RINEYVILLE",
    "ST. JOHN",
    "STAR MILLS",
    "STEPHENSBURG",
    "SUMMITT",
    "TIP TOP",
    "TUNNEL HILL",
    "VERTREES",
    "WHITE MILLS",
    "YOUNGERS CREEK",
    
    // Mead County
    "EKRON",
    "GUSTON"
  };
}
