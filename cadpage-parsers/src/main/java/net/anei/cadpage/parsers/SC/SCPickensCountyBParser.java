package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCPickensCountyBParser extends FieldProgramParser {
  
  public SCPickensCountyBParser() {
    super("PICKENS COUNTY", "SC", 
          "Location:ADDRCITY! ( Location_info:X! | Common_Location:X! ) CAD_Code:ID! Received:SKIP! Units:UNIT! Priority:PRI? Box_Alarm:BOX? Caller_Name:NAME? Caller:PHONE? Contact:SKIP? Case_Number:ID/L! Notes:INFO/N+");
    setupProtectedNames("J AND D DR", "LOVE AND CARE RD");
  }
  
  @Override
  public String getFilter() {
    return "pickenscountyE911@outlook.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_GPS_PTN = Pattern.compile("((?:\\d{2}\\.\\d{6})(-\\d{2}\\.\\d{6})|NoneNone) +(.*)");
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private static final Pattern ADDR_ST_PTN = Pattern.compile("[A-Z]{2}");
  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.matches()) {
        String gps1 = match.group(1);
        String gps2 = match.group(2);
        field = match.group(3);
        if (gps1 != null) setGPSLoc(gps1+','+gps2, data);
      }
      
      String zip = null;
      match = ADDR_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        zip = match.group(2);
      }
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (ADDR_ST_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      data.strCity =  city;
      super.parse(p.get(),  data);
      
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return "GPS ADDR APT CITY ST";
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
  
  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      for (String unit : field.split("; *")) {
        unit = unit.trim();
        unit = stripFieldStart(unit, "-");
        data.strUnit = append(data.strUnit, ",", unit);
      }
    }
  }

  private static final Pattern INFO_PREFIX_PTN = Pattern.compile(" *(?:; |^)\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_PREFIX_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
