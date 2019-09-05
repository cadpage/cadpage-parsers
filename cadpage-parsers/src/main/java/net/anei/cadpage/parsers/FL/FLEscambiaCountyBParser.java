package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

public class FLEscambiaCountyBParser extends FieldProgramParser {
  
  public FLEscambiaCountyBParser() {
    super("ESCAMBIA COUNTY", "FL",
          "( Rep#:ID INFO/NR! INFO/N+ " +
          "| ID ADDR APT X X CODE CALL! )");
  }
  
  @Override
  public String getFilter() {
    return "7017710278";
  }
  
  private CodeTable CALL_TABLE = new StandardCodeTable(); 
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("Escambia County Public Safety: ")) return false;
    body = body.substring(31).trim();
    body = stripFieldEnd(body, " Text STOP to opt out");
    if (body.startsWith("*"))  body = ' ' + body;
    if (!parseFields(body.split(" \\*"), data)) return false;
    String call = CALL_TABLE.getCodeDescription(data.strCode);
    if (call !=  null) data.strCall = call;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}-\\d{3}|", true);
    return super.getField(name);
  }

}
