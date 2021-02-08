package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class FLGulfCountyParser extends FieldProgramParser {

  public FLGulfCountyParser() {
    super("GULF COUNTY", "FL",
          "( SELECT/RR CAD#:ID! EMPTY CALL ADDR RX:DATETIME! INFO! INFO/N+ UNIT:UNIT! END " +
          "| CALL! ADDRESS:ADDR! PLACE RX:DATETIME! CAD#:ID! UNIT:UNIT END )");
  }

  @Override
  public String getFilter() {
    return "smartcop@gcso.fl.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("RUN REPORT")) {
      setSelectValue("RR");
      data.msgType = MsgType.RUN_REPORT;
    }
    else if (subject.equals("CALL FOR SERVICE") || subject.equals("SQL Server Message")) {
      setSelectValue("");
    }
    else return false;

    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
