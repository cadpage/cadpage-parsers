package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXCookeCountyAParser extends FieldProgramParser {
  
  public TXCookeCountyAParser() {
    super("COOKE COUNTY", "TX", 
        "CALL Description:INFO! INFO/N+? ADDR/Z CITY/Z! CrossStreets:X! PLACE! Description:INFO! INFO/N+ Dispatch:DATETIME! Call_Number:ID! END");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@co.cooke.tx.us";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" Description:", "\nDescription:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d");
    return super.getField(name);
  }
  
  private static final Pattern INFO_MARK_PTN = Pattern.compile("\\[\\d\\d/\\d\\d/\\d{4} \\d\\d?:\\d\\d:\\d\\d [A-Z]+\\]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : INFO_MARK_PTN.split(field)) {
        part = part.trim();
        part = stripFieldEnd(part, "/");
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }
}
