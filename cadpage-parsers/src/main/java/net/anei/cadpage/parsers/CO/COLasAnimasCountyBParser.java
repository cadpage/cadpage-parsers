package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COLasAnimasCountyBParser extends FieldProgramParser {

  public COLasAnimasCountyBParser() {
    super("LAS ANIMAS COUNTY", "CO",
          "CALL! ADDR! APT? CITY Note:INFO END");
  }

  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Alert")) return false;
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("FIRE.*", true);
    if (name.equals("APT")) return new AptField("Apt +(.*)", true);
    return super.getField(name);
  }
}
