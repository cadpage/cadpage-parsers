package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLEscambiaCountyBParser extends FieldProgramParser {

  public FLEscambiaCountyBParser() {
    super("ESCAMBIA COUNTY", "FL",
          "( SELECT/NEW ID ID/L UNIT ADDR APT APT PLACE X X CODE CALL CITY! GPS1 GPS2 END " +
          "| Rep#:ID INFO/NR! INFO/N+ " +
          "| ID ( EMPTY UNIT | ) ADDR APT ( EMPTY PLACE | ) X X CODE CALL! CITY GPS1 GPS2 EMPTY END " +
          ")");
  }

  @Override
  public String getFilter() {
    return "7017710278";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
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
    if (!parseFields(body.split(" \\*", -1), data)) return false;
    String call = FLEscambiaCountyParser.CALL_CODES.getCodeDescription(data.strCode);
    if (call !=  null) data.strCall = call;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("A{0,2}\\d{9,10}(?:-\\d{3})?|", true);
    return super.getField(name);
  }

}
