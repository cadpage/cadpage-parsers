package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

public class NYMadisonCountyCParser extends FieldProgramParser {
  
  public NYMadisonCountyCParser() {
    super("MADISON COUNTY", "NY", 
          "Location:ADDRCITY/SP! Response_Type:CALL! StatusTime:DATETIME! Agency_Name:SRC! INFO/N+");
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "e911@madisoncounty.ny.gov";
  }
  
  private static final Pattern JUNK_PTN = Pattern.compile("(?:=09|=20)+(?=\n|$)");
  private static final Pattern MARKER = Pattern.compile("Fire Dispatch Notification\\n+Response\\n\\s*");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    body = body.replace("\u00bf", "");
    if (subject.equals("Notification")) {
      Matcher match = MARKER.matcher(body);
      if (!match.lookingAt()) return false;
      body = body.substring(match.end());
    } else {
      data.strSource = subject;
      body = JUNK_PTN.matcher(body).replaceAll("");
    }
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d\\d \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      
      String apt = "";
      int pt = field.lastIndexOf(';');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }

      if (field.endsWith(")")) {
        pt = field.indexOf('(');
        if (pt >= 0) {
          String cross = field.substring(pt+1, field.length()-1).trim();
          cross = stripFieldStart(cross, "/");
          cross = stripFieldEnd(cross, "/");
          data.strCross = cross;
          field = field.substring(0, pt).trim();
        }
      }
      
      pt = field.indexOf('#');
      if (pt >= 0) {
        apt = append(field.substring(pt+1).trim(), "-", apt);
        field = field.substring(0,pt).trim();
      }
      
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }
  
  private static final CodeTable CALL_CODES = new StandardCodeTable();
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) *- *(.*)");
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        String call = CALL_CODES.getCodeDescription(data.strCode);
        if (call != null) field = call;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("Incident Notes|Response Notes|TimeStamp\\s+Info");
  private static final Pattern INFO_PTN = Pattern.compile("\\d\\d?/\\d\\d/\\d\\d\\s+\\d\\d?:\\d\\d:\\d\\d\\s+(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      
      Matcher match = INFO_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    city = stripFieldEnd(city, "-SUNY");
    city = stripFieldEnd(city, " VILLAGE");
    return city;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BEAVER CREEK",
      "BRONDER HOLLOW",
      "CARRIAGE HOUSE",
      "CHEESE FACTORY",
      "CHESTNUT RIDGE",
      "COBB HILL",
      "DAVIS CORNERS",
      "DELPHI FALLS",
      "DITCH BANK",
      "DONALD HICKS DEW",
      "EATON BROOK",
      "FENNER EAST",
      "GRASSY LANE",
      "INDIAN OPENING",
      "INGALLS CORNERS",
      "JAIL HOUSE",
      "LELAND G WRIGHT",
      "LELAND STRONG",
      "LEWIS POINT",
      "MAIN STREET",
      "MEADOW HILL",
      "MILE STRIP",
      "MOUNT HOPE",
      "MOUNT PLEASANT",
      "MUTTON HILL",
      "NELSON HEIGHTS",
      "NORTH COURT",
      "NORTH SIDE",
      "ONEIDA LAKE",
      "ONEIDA VALLEY",
      "ORAN DELPHI",
      "PARKER HILL",
      "PINE RIDGE",
      "PLEASANT VALLEY",
      "POMPEY CENTER",
      "REMINGTON PARK",
      "SOUTH CIRCLE",
      "SOUTH PETERBORO",
      "SOUTH VILLAGE",
      "STONE QUARRY",
      "WILLIAMS CORNERS",
      "WILLOW ACRES",
      "WILSON COVE"
  };
}
