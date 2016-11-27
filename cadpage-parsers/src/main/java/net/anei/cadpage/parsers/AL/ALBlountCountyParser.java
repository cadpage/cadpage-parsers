package net.anei.cadpage.parsers.AL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class ALBlountCountyParser extends FieldProgramParser {
  
  public ALBlountCountyParser() {
    super("BLOUNT COUNTY", "AL", 
          "CALL:CALL! ADDR:ADDR! CITY:CITY! XY:GPS? ID:ID! PRI:PRI? DATE:DATE! TIME:TIME! UNIT:UNIT INFO:X");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@blount911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern DELIM = Pattern.compile(",?\n");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldEnd(body, ",");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@',  '/').replace("*", "");
      super.parse(field, data);
    }
  }
}
