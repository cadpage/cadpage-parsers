
package net.anei.cadpage.parsers.FL;


import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class FLCrestviewParser extends FieldProgramParser {


  public FLCrestviewParser() {
    super("Crestview", "FL",
        "Call_Number:ID! Inc_Number:SKIP! Units:UNIT! Complaint:CALL! Location:PLACE! Address:ADDR! Disposition:SKIP! Box:BOX! Time_Dispatched:DATETIME! Narrative:INFO! This_Unit:SKIP!");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }


  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("_", " "); 
    if (!parseFields(body.split("\\|"), 10, data)) return false;
    
    // They do weird things splitting address up between Address and Location fields :(
    if (NUMERIC.matcher(data.strAddress).matches()) {
      data.strAddress = append(data.strAddress, " ", data.strPlace);
      data.strPlace = "";
    }
    return true;
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(",")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new DateTimeField(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
    return super.getField(name);
  }
}