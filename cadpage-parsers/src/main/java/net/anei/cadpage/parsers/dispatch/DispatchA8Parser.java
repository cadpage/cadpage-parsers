package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA8Parser extends FieldProgramParser {

  private static final Pattern DELIM = Pattern.compile(" \\*\\*(?: |$)");
  
  protected DispatchA8Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  protected DispatchA8Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
           "DISPATCH? TIME CALL ADDR PLACE ( MAP X! | NAME PHONE! MAP ) ( DATE SPECIAL CITY X UNIT | INFO DATE SPECIAL CITY X UNIT | EMPTY/Z INFO DATE CODE ID SRC SPECIAL ( MAP EMPTY EMPTY | CITY EMPTY SRC ) EMPTY X EMPTY UNIT | EMPTY SPECIAL EMPTY CODE ID EMPTY )");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body, -1), 6, data);
  }
  
  // Place field needs to strip off trailing dash
  // and is complete ignored if place was set in address field parsing
  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("-")) field = field.substring(1).trim();
      if (field.endsWith("-")) field = field.substring(0, field.length()-1).trim();
      
      if (field.startsWith("APT ")) {
        int pt = field.indexOf(',', 4);
        if (pt < 0) pt = field.indexOf(' ', 4);
        if (pt < 0) pt = field.length();
        data.strApt = field.substring(4,pt).trim();
        field = field.substring(Math.min(pt+1, field.length())).trim();
      }
      
      if (field.equals(data.strPlace)) return;
      data.strPlace = append(data.strPlace, " - ", field);
    }
  }
  
  private class BaseMapField extends MapField {
    public BaseMapField() {
      super("\\d\\d-[A-Z0-9]\\d|\\d+[A-Z] [A-Z]|\\d-\\d [A-Z]", false);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strMap)) return;
      super.parse(field, data);
    }
  }
  
  private class BaseSpecialField extends Field {

    @Override
    public void parse(String field, Data data) {
      parseSpecialField(field, data);
    }

    @Override
    public String getFieldNames() {
      return specialFieldNames();
    }
  }
  
  protected void parseSpecialField(String field, Data data) {
    
  }
  
  protected String specialFieldNames() {
    return "";
  }
  
  private class BaseSourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      if (data.strSource.length() == 0) data.strSource = field;
    }
  }
  
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      // Cross street info from special field is more reliable
      if (data.strCross.length() > 0) return;
      super.parse(field, data);
    }
  }
  
  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      // Unit information from special field is more reliable than official unit field
      if (data.strUnit.length() > 0) return;
      if (field.endsWith(",")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("DISPATCH")) return new SkipField("Dispatch", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d(?::\\d\\d)?|", true);
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("MAP")) return new BaseMapField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("SRC")) return new BaseSourceField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("SPECIAL")) return new BaseSpecialField();
    return super.getField(name);
  }
  
}
	