package net.anei.cadpage.parsers.FL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLEscambiaCountyBParser extends FieldProgramParser {

  public FLEscambiaCountyBParser() {
    super("ESCAMBIA COUNTY", "FL",
          "( Rep#:ID INFO/NR! INFO/N+ " +
          "| ID ( ID UNIT ADDR APT APT PLACE X X CODE CALL CITY! GPS1 GPS2 INFO/N+" +
               "| ADDR APT X X CODE CALL! END " +
               ") " +
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

    body = stripFieldStart(body, "Escambia County Public Safety:");
    body = stripFieldEnd(body, "Text STOP to opt out");

    if (body.startsWith("*"))  body = ' ' + body;
    if (!parseFields(body.split(" \\*", -1), data)) return false;
    String call = FLEscambiaCountyParser.CALL_CODES.getCodeDescription(data.strCode);
    if (call !=  null) data.strCall = call;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }

  private static final Pattern ID_DELIM_PTN = Pattern.compile("; *");

  private class MyIdField extends IdField {
    public MyIdField() {
      super("A{0,2}\\d{9,10}(?:-\\d{3})?(?:;.*)?|", true);
    }

    @Override
    public void parse(String field, Data data) {
      field = ID_DELIM_PTN.matcher(field).replaceAll("/");
      data.strCallId = append(data.strCallId, "/", field);
    }
  }

}
