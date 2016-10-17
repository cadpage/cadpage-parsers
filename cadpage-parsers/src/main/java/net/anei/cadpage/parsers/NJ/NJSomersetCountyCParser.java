package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Somerset County, NJ
 */
public class NJSomersetCountyCParser extends FieldProgramParser {
  
  public NJSomersetCountyCParser() {
    super("SOMERSET COUNTY", "NJ",
           "CALL ADDR/SXP X CITY UNIT! ");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\\|"), 5, data);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strCode = field.substring(0,pt).trim();
        field = field.substring(pt+3).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
}



