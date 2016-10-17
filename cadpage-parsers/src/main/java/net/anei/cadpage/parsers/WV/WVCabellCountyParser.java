package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class WVCabellCountyParser extends FieldProgramParser {
  
  private static final Pattern MARKER_PTN = Pattern.compile("^CCERC911\n:");
  
  public WVCabellCountyParser() {
    super("CABELL COUNTY", "WV",
           "ID DATE TIME CALL ADDR CITY! PLACE? X X? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@ccerc911.org,7777";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER_PTN.matcher(body);
    if (match.find()) body = body.substring(match.end()).trim();
    return parseFields(body.split("/"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("");
    if (name.equals("DATE")) return new MyDateField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyDateField extends DateField {
    public MyDateField() {
      setPattern("\\d\\d-\\d\\d-\\d\\d", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace('-', '/'), data);
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(" INTERSECTN")) field = field.substring(0,field.length()-11).trim();
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("END") || field.equals("RR TRACKS")) {
        parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }
}
