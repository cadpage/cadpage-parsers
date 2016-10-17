package net.anei.cadpage.parsers.MI;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Clinton County, MI
 */
public class MIClintonCountyParser extends DispatchOSSIParser {
  
  public MIClintonCountyParser() {
    super("CLINTON COUNTY", "MI",
          "( CANCEL ADDR! SKIP | ADDR X/Z+? CALL! PRI? ) INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@clinton-county.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern CALL_PTN = Pattern.compile(".* [1-4]|FIRE .*|LIFT ASSIST.*|MUTUAL AID.*|PERSONAL INJURY.*|STRUCTURE FIRE|SUICIDE.*");
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    public boolean checkParse(String field, Data data) {
      if (!getRelativeField(+1).equals("1") &&
          !CALL_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }
    
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
