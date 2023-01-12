package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Edina, MN
 */
public class MNEdinaBParser extends FieldProgramParser {
  public MNEdinaBParser() {
    super("EDINA", "MN",
          "DATE:DATETIME! CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! OTHER_LOCATION_INFO:INFO! CROSS_STREETS:X! UNIT:UNIT! INFO:INFO! INFO/N+ INCIDENT_NUMBER:ID! END");
  }

  public String getFilter() {
    return "CAD@ci.edina.mn.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String last = "";
      for (String part : field.split(",")) {
        part = part.trim();
        if (part.equals(last)) continue;
        data.strSupp = append(data.strSupp, ", ", part);
        last = part;
      }
    }
  }
}
