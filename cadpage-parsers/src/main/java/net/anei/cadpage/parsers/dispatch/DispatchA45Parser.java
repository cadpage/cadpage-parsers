package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class DispatchA45Parser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile(" \\*(?= )");
  
  public DispatchA45Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "ADDR CITY CALL! Dispatch:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "@northumberland.alertpa.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("desc ")) body = body.substring(5).trim();
    int pt = body.indexOf("\nSent by ");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace('\n', ' ');
    return parseFields(DELIM.split(body), data);
  }
  
  private static final Pattern UNIT_SPACE_PTN = Pattern.compile("(?<=[A-Z]) +(?=\\d)");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_SPACE_PTN.matcher(field).replaceAll("_");
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
}
