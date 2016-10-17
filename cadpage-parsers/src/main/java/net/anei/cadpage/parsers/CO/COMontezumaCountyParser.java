package net.anei.cadpage.parsers.CO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COMontezumaCountyParser extends FieldProgramParser {
  
  public  COMontezumaCountyParser() {
    super(CITY_LIST, "MONTEZUMA COUNTY", "CO", 
          "ID DATE/d TIME ( CALL ADDR/Z CITY | CALL CALL/L ADDR/Z CITY | CALL CALL2/L? ADDR ) ( PLACE X/Z X/Z INFO SRC! | PLACE X/Z X/Z SRC! | PLACE_X/Z X/Z SRC! | PLACE_X/Z SRC! | SRC! | X X? | PLACE X X? | X+? ) INFO/SLS+? UNIT! END");
    removeWords("RR");   // RR is a unit, not a cross street
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith(":")) return false;
    return parseFields(body.substring(1).trim().split("/"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{9}", true);
    if (name.equals("CALL2")) return new CallField("Gas", true);
    if (name.equals("DATE")) return new DateField("\\d\\d-\\d\\d-\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("PLACE_X")) return new MyPlaceCrossField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("SRC")) return new MySourceField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, " INTERSECTN");
      super.parse(field, data);
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("WAS ")) {
        data.strSupp = field;
      } else {
        data.strPlace = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE INFO?";
    }
  }

  private static final Pattern CROSS_PTN = Pattern.compile("MM.*|ROAD.*|DEAD END");
  private class MyPlaceCrossField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (CROSS_PTN.matcher(field).matches() || isValidAddress(field)) {
        data.strCross = field;
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (CROSS_PTN.matcher(field).matches()) {
        super.parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }
  
  private class MySourceField extends SourceField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!DEPT_SET.contains(field)) return false;
      data.strSource = field;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Set<String> DEPT_SET = new HashSet<String>(Arrays.asList(new String[]{
      "CORTEZ",
      "DOLORES",
      "LEW-ARR",
      "MANCOS",
      "MVP",
      "PLV",
      "RICO"
  }));
  
  private static final String[] CITY_LIST = new String[]{
    "ARRIOLA",
    "CAHONE",
    "CORTEZ",
    "DOLORES",
    "LEWIS",
    "MANCOS",
    "MESA VERDE",
    "PLEASANT VIEW",
    "TOWAOC",
    "YELLOW JACKET",
    
    // Delores County
    "CAHONE",
    "DOVE CREEK",
    "RICO"
  };
}
