package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PAMontgomeryCountyAParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile(" *\\* *");
  
  public PAMontgomeryCountyAParser() {
    super("MONTGOMERY COUNTY", "PA",
           "TIME CALL ADDR PLACE");
  }
  
  @Override
  public String getFilter() {
    return "alert46@Verizon.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Dispatch")) return false;
    String[] flds = DELIM.split(body);
    if (flds.length < 4) return false;
    return parseFields(flds, data);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("-")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }
}
