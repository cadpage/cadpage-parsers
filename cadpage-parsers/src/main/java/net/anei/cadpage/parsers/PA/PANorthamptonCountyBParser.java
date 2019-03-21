package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PANorthamptonCountyBParser extends FieldProgramParser {
  
  public PANorthamptonCountyBParser() {
    super("NORTHAMPTON COUNTY", "PA", 
          "UNIT_CALL_ADDR_CITY! Cross_Streets:X! Caller:NAME! Case:ID! END");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@onsolve.com,76993";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT_CALL_ADDR_CITY"))  return new MyUnitCallAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private static final Pattern UNIT_CALL_ADDR_PTN = Pattern.compile("([A-Z]+\\d+) ([^-]*) - (.*)");
  private class MyUnitCallAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_CALL_ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strUnit = match.group(1);
      data.strCall = match.group(2).trim();
      super.parse(match.group(3).trim(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT CALL " + super.getFieldNames();
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
