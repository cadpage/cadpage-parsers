package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCNashCountyCParser extends FieldProgramParser {
  
  public NCNashCountyCParser() {
    super("NASH COUNTY", "NC", 
          "CALL ADDRCITY PLACE GPS1 GPS2 NAME UNIT/C+? ID DATETIME! INFO/N+");
  }
  
  public String getFilter() {
    return "donotreply@nashcountync.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    if (!parseFields(body.split(";"), data)) return false;
    if (data.strCall.length() == 0) data.strCall = subject;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("ID"))  return new IdField("CFS\\d\\d-\\d{6}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d?:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_ZIP_CH_PTN = Pattern.compile("(.*?)(?: +(\\d{5}))?(?: +(TAC\\d+))?");
  private static final Pattern ST_PTN = Pattern.compile("[A-Z]{2}");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ZIP_CH_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        zip =  match.group(2);
        data.strChannel = match.group(3);
      }
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (ST_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      if (city.length() == 0 && zip != null) city = zip;
      data.strCity = city;
      
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST CH";
    }
  }
  
  private class MyPlaceField extends PlaceField {
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
}
