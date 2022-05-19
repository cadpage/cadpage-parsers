package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PANorthamptonCountyBParser extends FieldProgramParser {
  
  public PANorthamptonCountyBParser() {
    super("NORTHAMPTON COUNTY", "PA", 
          "UNIT_CALL_ADDR_CITY/S6! ( XST:X! CALLER:NAME! CASE:ID! NARR:INFO! | Cross_Streets:X! Caller:NAME! Case:ID! ) END");
    setupSpecialStreets("BROADWAY", "RAMP");
    addCrossStreetNames("ALLEY ALY", "UNNAMED");
    removeWords("NEW", "PARK");
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
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern UNIT_CALL_ADDR_PTN = Pattern.compile("([A-Z]+\\d+) (.*?) - (.*)");
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
      field = field.replace('@',  '/');

      if (field.startsWith("No Cross Streets Found")) {
        data.strPlace = field.substring(22).trim();
        return;
      }
      
      String prefix = null;
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        prefix = field.substring(0,pt);
        field = field.substring(pt+1);
      }
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_EMPTY_ADDR_OK, field, data);
      if (!isValidAddress()) {
        if (!data.strCross.equals(data.strUnit)) data.strPlace = data.strCross;
        data.strCross = "";
      }
      if (prefix != null) data.strCross = append(prefix, ", ", data.strCross);
      String place = getLeft();
      if (place.equals(data.strUnit)) place = "";
      data.strPlace = append(data.strPlace, " ", place);
      
      if (data.strPlace.equals("LUKE'S NORTH") && data.strCross.endsWith(" ST")) {
        data.strPlace = "ST " + data.strPlace;
        data.strCross = data.strCross.substring(0, data.strCross.length()-3).trim();
      }
    }
    
    @Override
    public String getFieldNames() {
      return "X PLACE";
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
  
  private static final Pattern INFO_PFX_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} \\d{4} +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        Matcher match = INFO_PFX_PTN.matcher(line);
        if (match.lookingAt()) line = line.substring(match.end());
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
}
