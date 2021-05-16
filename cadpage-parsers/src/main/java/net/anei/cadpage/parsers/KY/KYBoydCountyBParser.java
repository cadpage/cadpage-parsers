package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;


public class KYBoydCountyBParser extends FieldProgramParser {

  public KYBoydCountyBParser() {
    super("BOYD COUNTY", "KY",
          "UNIT UNIT/S+? CODE_CALL ( EMPTY ID! | ADDR APT? CITY ID! ) INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]{2,5}", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ID"))  return new IdField("\\d{12}", true);
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]{2,5}) - +(.*)");
  private class MyCodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }

  }
}
