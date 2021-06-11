package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYNassauCountyNParser extends FieldProgramParser {

  public NYNassauCountyNParser() {
    super("NASSAU COUNTY", "NY",
          "CALL:CALL! ADDR:ADDR! CITY:CITY! MAP:MAP CROSS:X ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNITS:UNIT SOURCE:SKIP! CAD:ID? INFO:INFO? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "root@wlvacems.com,Mark@MimoCAD.io,dygear@mimocad.io";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new MyDateField();
    return super.getField(name);
  }

  private static final Pattern DATE_PTN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
  private class MyDateField extends DateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
    }
  }
}
