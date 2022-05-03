package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDQueenAnnesCountyDParser extends FieldProgramParser {

  public MDQueenAnnesCountyDParser() {
    super("QUEEN ANNES COUNTY", "MD",
          "CALL:CALL! ADDR:ADDR! DCITY:CITY? DST:ST! PL:PLACE! X:X! UNIT:UNIT! CH:CH! MAP:MAP! GPS:GPS? INFO:INFO! INFO/N+ CODE:CODE DATETIME:DATETIME");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CMalert")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d|", true);
    return super.getField(name);
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Box Area:");
      super.parse(field,  data);
    }
  }

}
