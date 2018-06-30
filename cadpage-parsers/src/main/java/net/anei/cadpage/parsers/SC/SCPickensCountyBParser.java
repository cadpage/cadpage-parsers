package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCPickensCountyBParser extends FieldProgramParser {
  
  public SCPickensCountyBParser() {
    super("PICKENS COUNTY", "SC", 
          "Location:ADDRCITY! Common_Location:X! CAD_Code:ID! Received:SKIP! Units:UNIT! Case_Number:ID/L! Notes:INFO/N+");
    setupProtectedNames("J AND D DR", "LOVE AND CARE RD");
  }
  
  @Override
  public String getFilter() {
    return "pickenscountyE911@outlook.com";
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
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private static final Pattern ADDR_ST_PTN = Pattern.compile("[A-Z]{2}");
  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ZIP_PTN.matcher(field);
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
      return "ADDR APT CITY ST";
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
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
