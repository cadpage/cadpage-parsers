package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYNassauCountyAParser extends FieldProgramParser {

  public NYNassauCountyAParser() {
    super(CITY_LIST, "NASSAU COUNTY", "NY",
           "ADDR/SP! CS:X! ADTNL:INFO GRID:MAP TOA:TIMEDATE");
    setAllowDirectionHwyNames();
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "@rednmxcad.com,paging@alpinesoftware.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    int pt1 = body.indexOf("***");
    if (pt1 < 0) return false;
    pt1 += 3;
    int pt2 = body.indexOf("*** ", pt1);
    if (pt2 < 0) return false;
    data.strCall = body.substring(pt1, pt2).trim();
    body = body.substring(pt2+4).trim();
    body = body.replace(" c/s:", " CS:");
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    return super.getField(name);
  }

  private static final Pattern CALL_ADDR_PTN = Pattern.compile("([A-Z]{3,5}) +(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_ADDR_PTN.matcher(field);
      if (match.matches()) {
        data.strCall = data.strCall + " - " + match.group(1);
        field = match.group(2);
      }
      Parser p = new Parser(field);
      field = p.get('[');
      String city = p.get(']');
      super.parse(field, data);
      if (city.length() > 0) data.strCity = city;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strMap = field.substring(pt+3).trim().replaceAll("  +", " ");
        field = field.substring(0,pt).trim();
      }
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "X MAP";
    }
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("^\\d\\d:\\d\\d\\b");
  private static final Pattern DATE_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d{4}");
  private class MyTimeDateField extends TimeDateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_PTN.matcher(field);
      if (!match.find()) return;
      data.strTime = match.group();
      field = field.substring(match.end()).trim();
      if (!DATE_PTN.matcher(field).matches()) return;
      data.strDate = field;
    }
  }
  
  private static final String[] MWORD_STREET_LIST = new  String[]{
    "B GATE",
    "GLEN COVE",
    "HARBOR HILL",
    "HARBOR PARK",
    "HIGH HOLLOW",
    "HITCHCOCK (E)",
    "HITCHCOCK (W)",
    "INDUSTRIAL PARK",
    "JOHN BEAN",
    "LONG ISLAND",
    "MAIN CAMPUS",
    "MIDDLE NECK",
    "NORTHERN STATE",
    "O ANDOVER",
    "PARK (E)",
    "PINE (S)",
    "PLANTING FIELD",
    "PORT WASHINGTON",
    "RED GROUND",
    "ROUND HILL",
    "SERVICE (S)",
    "SHORE (W)",
    "SPRING HILL",
    "ST MARKS",
    "STORE HILL"
 
  };
  
  private static final String[] CITY_LIST = new String[]{
    "EAST HILLS",
    "FLORAL PARK",
    "GREENVALE",
    "OLD WESTBURY",
    "ROSLYN ESTATES",
    "ROSLYN HEIGHTS",
  };
}


