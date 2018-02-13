package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class ALBlountCountyParser extends FieldProgramParser {
  
  public ALBlountCountyParser() {
    super("BLOUNT COUNTY", "AL", 
          "CALL:CALL! ADDR:ADDR! CITY:CITY! XY:GPS? ID:ID! PRI:PRI? DATE:DATE! TIME:TIME! UNIT:UNIT ( X:X INFO:INFO | INFO:XINFO )");
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
    if (name.equals("XINFO")) return new MyCrossInfoField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@',  '/').replace("*", "");
      super.parse(field, data);
    }
  }
  
  private static final Pattern CROSS_INFO_PTN = Pattern.compile("([^a-z]*?)(?:^(?=\\d+ *[A-Z]?[a-z])| (?=[A-Z][a-z])|(?<=[A-Z])(?=[1-9])|(?=[a-z]))(.*)");
  private class MyCrossInfoField extends MyCrossField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CROSS_INFO_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strSupp = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "X INFO";
    }
  }
}
