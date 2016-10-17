package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LACalcasieuParishParser extends FieldProgramParser {
  
  public LACalcasieuParishParser() {
    super("CALCASIEU PARISH", "LA", 
          "CALL CALL2/S? ADDR PLACE? CITY! X+? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "e911page@cityoflc.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("E911400")) return false;
    body = body.replace("\n", "");
    return super.parseFields(body.split("/",-1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new CallField("RELEASE", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field,  " INTERSECTN");
      super.parse(field, data);
    }
  }
  
  private static final Pattern UNIT_CITY_PTN = Pattern.compile("([A-Z]+\\d+) (.*)");
  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_CITY_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = match.group(1);
      data.strCity = match.group(2).trim();
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT CITY";
    }
  }
}
