package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH01Parser;


public class FLHernandoCountyParser extends DispatchH01Parser {
  public FLHernandoCountyParser() {
    super("HERNANDO COUNTY", "FL",
          "( MARK! Workstation:SKIP! Print_Time:SKIP! User:SKIP! Location:ADDR! Response_Type:CALL! Zone_Name:MAP! Priority_Name:PRI Creation_Time:SKIP! Sequence_Number:ID! Status_Name:SKIP! Status_Time:DATETIME! Handling_Unit:UNIT! Agency:SRC! NOTES+ " +
          "| STATUS CALL ADDR MAP UNIT! )");
  }

  @Override
  public String getFilter() {
    return "@hernandosheriff.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Notification") && !subject.equals("Notification")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseFields(String[] fields, Data data) {
    if (fields.length == 0) return false;
    if (fields.length <= 2) fields = fields[0].split("\n");
    return super.parseFields(fields, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MARK")) return new SkipField("Response Email Report lite", true);
    if (name.equals("PRI")) return new PriorityField("(\\d) Priority \\d", true);
    if (name.equals("STATUS")) return new SkipField("Dispatch|Responding");
    return super.getField(name);
  }
}