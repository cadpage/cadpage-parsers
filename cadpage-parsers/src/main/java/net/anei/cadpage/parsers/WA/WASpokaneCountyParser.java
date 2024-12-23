package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WASpokaneCountyParser extends FieldProgramParser {

  public WASpokaneCountyParser() {
    super("SPOKANE COUNTY", "WA",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! TAC:CH? UNIT:UNIT! MAP:MAP? CROSS:X? INFO:INFO/N+ LAT:GPS1 LON:GPS2");
  }

  @Override
  public String getFilter() {
    return "noreply@spokanecity.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Message from CAD")) {
      data.strCall = "GENERAL ALERT";
      data.strPlace = body;
      return true;
    }

    if (subject.toUpperCase().replace("=20", " ").contains("ASSIGNED TO INCIDENT ") ) {
      return parseFields(body.split("\n"), data);
    }

    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCodeCallField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+)- *(.*)");
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

  private class MyCrossField extends CrossField {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }

  }
}
