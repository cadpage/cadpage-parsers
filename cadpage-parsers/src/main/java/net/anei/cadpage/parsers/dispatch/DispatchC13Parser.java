package net.anei.cadpage.parsers.dispatch;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchC13Parser extends FieldProgramParser {

  public DispatchC13Parser(String defCity, String defState) {
    super(defCity, defState,
          "Units:UNIT! Response_Type:CALL? Call_Type:CALL/SDS! Loc.Name:PLACE! Area:MAP! Street:ADDR! X.Street:X! Apt#:APT! Bld#:APT2! Latitude:GPS1 Longitude:GPS2 Call_Comments:INFO INFO/N+ END");
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
