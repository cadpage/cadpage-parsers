package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXCaldwellCountyParser extends FieldProgramParser {

  public TXCaldwellCountyParser() {
    super("CALDWELL COUNTY", "TX",
          "ID CALL! ADDR City:CITY? Postal_Code:ZIP? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Alert")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    return super.getField(name);
  }

}
