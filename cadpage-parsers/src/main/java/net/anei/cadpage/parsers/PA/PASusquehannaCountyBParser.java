package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PASusquehannaCountyBParser extends FieldProgramParser {

  public PASusquehannaCountyBParser() {
    super("SUSQUEHANNA COUNTY", "PA",
          "CALL ADDRCITY! COMM_NAME:PLACE! Lat/Lon:GPS! XS:X! REMARKS:INFO INFO/N+ OPERATIONS_CHANNEL:CH! UNITS:UNIT! DATETIME! END");
  }

  @Override
  public String getFilter() {
    return "911alerts@susqco.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("SUSQ ALERT")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
