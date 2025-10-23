package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLOkaloosaCountyCParser extends FieldProgramParser {

  public FLOkaloosaCountyCParser() {
    super("OKALOOSA COUNTY", "FL",
          "Units:UNIT! Call_Type:CALL! Loc.Name:PLACE! Area:MAP! Street:ADDR! X.Street:X! Apt#:APT! Bld#:APT2! END");
  }

  @Override
  public String getFilter() {
    return "monitor@firstinalerting.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Alert:")) return false;
    body = body.replace(" Call Type:", "\nCall Type:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  protected boolean parseFields(String[] fields, Data data) {
    for (int j = 0; j<fields.length; j++) {
      String fld = fields[j].trim();
      int pt = fld.indexOf(':');
      if (pt >= 0) {
        String val = fld.substring(pt+1).trim();
        if (val.equals("n/a")) fields[j] = fld.substring(0,pt+1);
      }
    }
    return super.parseFields(fields, data);
  }
}
