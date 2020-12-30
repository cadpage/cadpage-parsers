package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

public class FLEscambiaCountyBParser extends FieldProgramParser {

  public FLEscambiaCountyBParser() {
    super("ESCAMBIA COUNTY", "FL",
          "( SELECT/NEW ID UNIT ADDR APT PLACE X X CODE CALL CITY END " +
          "| Rep#:ID INFO/NR! INFO/N+ " +
          "| ID ADDR APT X X CODE CALL! END " +
          ")");
  }

  @Override
  public String getFilter() {
    return "7017710278";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    body = stripFieldEnd(body, "Text STOP to opt out");

    if (body.startsWith("Escambia County Public Safety: ")) {
      setSelectValue("OLD");
      body = body.substring(31).trim();
    } else {
      setSelectValue("NEW");
    }
    if (body.startsWith("*"))  body = ' ' + body;
    if (!parseFields(body.split(" \\*"), data)) return false;
    String call = FLEscambiaCountyParser.CALL_CODES.getCodeDescription(data.strCode);
    if (call !=  null) data.strCall = call;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}(?:-\\d{3})?|", true);
    return super.getField(name);
  }

}
