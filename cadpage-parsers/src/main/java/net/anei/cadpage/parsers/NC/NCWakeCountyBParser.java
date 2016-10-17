package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCWakeCountyBParser extends DispatchOSSIParser {
  
  public NCWakeCountyBParser() {
    super(CITY_CODES, "CARY", "NC",
          "( CANCEL ADDR CITY PLACE " +
          "| FYI SRC1 SRC2? CALL ADDR X+? " +
          "| SRC1 SRC2? CALL ADDR X/Z+? UNIT! ) INFO+");
  }

  @Override
  public String getFilter() {
    return "CAD@townofcary.org";
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("SRC1")) return new MySourceField("[A-Z]{1,4}");
    if (name.equals("SRC2")) return new MySourceField("S\\d{2}|RALW");
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+,[A-Z0-9,]+|[A-Z]+\\d+|[A-Z]+FD|MUT[A-Z0-9]+", true);
    return super.getField(name);
  }
  
  private class MySourceField extends SourceField {
    public MySourceField(String pattern) {
      setPattern(pattern, true);
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strSource = append(data.strSource, ",", field);
    }
  }
  
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*) (\\d{1,2}[A-Z]\\d{1,2})");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCode = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }
  
  // City codes are only used for CANCEL messages :(
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "APEX", "APEX",
      "CARY", "CARY",
      "MORR", "MORRISVILLE" 
  });
}