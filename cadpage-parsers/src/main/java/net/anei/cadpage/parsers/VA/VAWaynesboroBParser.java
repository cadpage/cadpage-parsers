package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class VAWaynesboroBParser extends DispatchOSSIParser {
  
  public VAWaynesboroBParser() {
    super("WAYNESBORO", "VA", 
          "( FYI CALL ADDR! X X PLACE | UNIT_CALL ADDR! WAYNDIST:SKIP? CALL/SDS ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@ci.waynesboro.va.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Reject VAWaynesboroA -> VAAugustaCounty calls
    if (subject.length() == 0) return false;
    
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT_CALL")) return new MyUnitCallField();
    return super.getField(name);
  }
  
  private static final Pattern UNIT_CALL_PTN = Pattern.compile("\\{([A-Z0-9]+)\\} *(.*)");
  private class MyUnitCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strUnit = match.group(1);
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "UNIT CALL";
    }
  }

}
