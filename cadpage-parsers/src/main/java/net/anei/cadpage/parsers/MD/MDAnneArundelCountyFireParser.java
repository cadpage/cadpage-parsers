package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MDAnneArundelCountyFireParser extends FieldProgramParser {
  
  private static final Pattern SEPARATOR = Pattern.compile(" *\\| *");
  
  public MDAnneArundelCountyFireParser() {
    super("ANNE ARUNDEL COUNTY", "MD",
           "CITY SKIP CALL ADDR! INFO");
  }
  
  @Override
  public String getFilter() {
    return "alerts@alertpage.ealertgov.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    String[] flds = SEPARATOR.split(body);
    if (flds.length < 5) return false;
    if (!flds[1].equals("ANNE ARUNDEL") &&
        !flds[1].equals("PRINCE GEORGES")) return false;
    return parseFields(flds, data);
  }
  
  private static final Pattern UNIT1_PTN = Pattern.compile(" +([^ ]+\\d+)$");
  private static final Pattern UNIT2_PTN = Pattern.compile("^([^ ]+\\d+) +");
  private static final Pattern BOX_PTN = Pattern.compile("\\d+");
  private class MyCityField extends CityField {
    
    @Override
    public void parse(String field, Data data) {
      String sUnit = null;
      Matcher match = UNIT1_PTN.matcher(field);
      if (match.find()) {
        sUnit = match.group(1).trim();
        field = field.substring(0,match.start()).trim();
      } else {
        match = UNIT2_PTN.matcher(field);
        if (match.find()) {
          sUnit = match.group(1);
          field = field.substring(match.end()).trim();
        }
      }
      if (sUnit != null) {
        if (BOX_PTN.matcher(sUnit).matches()) {
          data.strBox = sUnit;
        } else {
          data.strUnit = sUnit;
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CITY BOX UNIT";
    }
  }
  
  @Override
  public  Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
}
