package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Ontario County, NY (B)
 */
public class NYOntarioCountyBParser extends FieldProgramParser {
  
  public NYOntarioCountyBParser() {
    super("ONTARIO COUNTY", "NY",
           "CALL:CALL! ADDR:ADDR! CITY:CITY! INFO:INFO! UNIT:UNIT! VN:SKIP!");
  }
  
  @Override
  public String getFilter() {
    return "no-reply@fingerlakesambulance.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Cadpage")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+)-(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("0-")) {
        data.strCall = "ALERT";
      }
      else {
        Matcher match = CODE_CALL_PTN.matcher(field);
        if (match.matches()) {
          data.strCode = match.group(1);
          data.strCall = match.group(2).trim();
        } else {
          data.strCall = field;
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
	