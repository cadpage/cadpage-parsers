package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNBradleyCountyBParser extends FieldProgramParser {

  public TNBradleyCountyBParser() {
    super("BRADLEY COUNTY", "TN",
          "CALL! Address:ADDR! Apt:APT");
  }

  @Override
  public String getFilter() {
    return "777,93001,1410,911center@clevelandtn911.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "CLEVELANDTN911:");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("Problem - +(.*)", true);
    return super.getField(name);
  }
}
