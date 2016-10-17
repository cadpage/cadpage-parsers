package net.anei.cadpage.parsers.OR;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ORJosephineCountyParser extends FieldProgramParser {
  
  private static final Pattern UNITS_PTN = Pattern.compile("Units: +");
  
  public ORJosephineCountyParser() {
    super("JOSEPHINE COUNTY", "OR",
          "( ID CALL ADDRCITY/SXa PLACE X/Z? SRC DATETIME! UNIT | DATETIME CALL ADDR_CITY_X/SXa! Units:UNIT | CALL ADDRCITY/SXa PLACE DATETIME ID! UNIT ) GPS1? GPS2? INFO/S+");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@Pacific.com";
  }
  
  private static final Pattern LAT_LON_PTN = Pattern.compile("\\bLAT: *([-+]?[\\d\\.]+),? +LON: *([-+]?[\\d\\.]+)\\b");
  private static final Pattern LAT_LON_PTN2 = Pattern.compile("\\bLAT:([-+]?[\\d\\.]+) LON:([-+]?[\\d\\.]+)\\b");
  private static final Pattern DELIM = Pattern.compile("\n|: |(?<=[AP]M)(:| {2,})");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0 && body.startsWith("!  / ")) {
      subject = "!";
      body = body.substring(5).trim().replace("=\n", "").replace("=0A", "\n");
    }
    
    if (! subject.trim().equals("!")) return false;
    body = LAT_LON_PTN.matcher(body).replaceAll("LAT:$1 LON:$2");
    body = UNITS_PTN.matcher(body).replaceFirst("Units:");
    String[] flds = DELIM.split(body);
    if (flds.length < 4) flds = body.split("  ");
    if (!parseFields(flds, data)) return false;
    data.strAddress = LAT_LON_PTN2.matcher(data.strAddress).replaceFirst("LAT: $1, LON: $2");
    return true;
  }
  
  private static final String GPS_PTN_STR = "[-+]?\\d{2,3}\\.\\d{6,}";

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d? [AP]M", DATE_TIME_FMT, true);
    if (name.equals("ADDR_CITY_X")) return new MyAddressCityCrossField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("SRC")) return new SourceField("\\d{4}(?:, *\\d{4})*");
    if (name.equals("ID")) return new IdField("\\d+|ODF", true);
    if (name.equals("GPS1")) return new GPSField(1, GPS_PTN_STR, true);
    if (name.equals("GPS2")) return new GPSField(2, GPS_PTN_STR, true);
    return super.getField(name);
  }
  
  private class MyAddressCityCrossField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get(',').replace('@', '&'), data);
      data.strCity = p.get("  ");
      String cross = p.get();
      if (!cross.equals("No Cross Streets Found")) data.strCross = cross;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY X";
    }
  }
  
  private static final Pattern INTERSECT_MARKER = Pattern.compile(" *@ *");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = INTERSECT_MARKER.matcher(field).replaceAll(" & ");
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
  

  @Override
  public String adjustMapAddress(String address) {
    // There is a street named JUMP OFF JOE CREED RD that trips the OFF mapping logic
    return address.replace("JUMP OFF", "JUMP0OFF");
  }

  @Override
  public String postAdjustMapAddress(String address) {
    return address.replace("JUMP0OFF", "JUMP OFF");
  }
}
