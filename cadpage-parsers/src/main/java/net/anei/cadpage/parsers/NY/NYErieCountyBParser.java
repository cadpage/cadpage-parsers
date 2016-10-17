package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYErieCountyBParser extends FieldProgramParser {
  
  public NYErieCountyBParser() {
    super("ERIE COUNTY", "NY",
           "SRC CITY CALL PLACE ADDR INFO UNIT TIME");
  }
  
  @Override
  public String getFilter() {
    return "IPN <IPN@IPN911.net>";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] flds = body.split("\\|");
    if (flds.length < 7) return false;
    return parseFields(flds, data);
  }
  
  private static final Pattern PAREN_PTN = Pattern.compile("\\((.*)\\)"); 
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PAREN_PTN.matcher(field);
      if (match.find()) field = match.group(1);
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
}
