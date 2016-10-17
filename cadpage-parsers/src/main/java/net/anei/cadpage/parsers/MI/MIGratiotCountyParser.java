package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIGratiotCountyParser extends FieldProgramParser {
  
  public MIGratiotCountyParser() {
    super("GRATIOT COUNTY", "MI", 
          "CALL:CALL! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "csbmail@coretechcorp.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Alert")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new MyDateField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d");
    return super.getField(name);
  }
  
  private static final Pattern DATE_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d)");
  private class MyDateField extends DateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
    }
  }

}
