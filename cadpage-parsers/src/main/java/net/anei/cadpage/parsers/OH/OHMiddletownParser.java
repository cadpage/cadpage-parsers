package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMiddletownParser extends FieldProgramParser {

  public OHMiddletownParser() {
    super("MIDDLETOWN", "OH",
          "dispatch:UNIT! location:ADDR! city:CITY! state:ST! callType:CALL! category:SKIP! narrative:CALL/L! coordinates:GPS! incidentId:ID! timestamp:SKIP! END");
  }

  @Override
  public String getFilter() {
    return "no-reply@bryx.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("[BRYX Dispatch]")) return false;
    return parseFields(body.split("\n"), data);
  }
}
