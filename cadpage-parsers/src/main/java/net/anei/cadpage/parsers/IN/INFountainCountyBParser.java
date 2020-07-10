package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INFountainCountyBParser extends FieldProgramParser {

  public INFountainCountyBParser() {
    super("FOUNTAIN COUNTY", "IN",
          "CALL:CODE_CALL! ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! MAP:MAP! UNIT:UNIT! INFO:INFO! DIRECTIONS:LINFO! WARNINGS:ALERT!");
  }

  @Override
  public String getFilter() {
    return "@fwrdc.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf(" - CALL:");
    if (pt < 0) return false;
    body = body.substring(pt+3);

    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{4}) +(.*)");
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
