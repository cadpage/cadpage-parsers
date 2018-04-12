package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VARadfordParser extends FieldProgramParser {
    
  public VARadfordParser() {
    super("RADFORD", "VA",
          "CALL:CALL! ADDR:ADDR! CITY:CITY! XY:GPS? ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! X:X? INFO:INFO! INFO/N+ END");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@radford.va.us";
  }
  
  private static final Pattern DELIM = Pattern.compile(",?\n");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("*", "").replaceAll("@", "/").replace("(Verify)", " ").trim();
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }
}