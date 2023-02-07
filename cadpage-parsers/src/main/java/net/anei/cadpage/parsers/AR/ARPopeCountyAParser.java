package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ARPopeCountyAParser extends FieldProgramParser {

  public ARPopeCountyAParser() {
    super("POPE COUNTY", "AR",
          "Call_Time:DATETIME! Service_Call_Type:CALL! Street_Address:ADDRCITY! Common_Name:PLACE! Latitude:GPS1! Longitude:GPS2! Cross_Streets:X! Emergency_Nature:CALL/SDS! Station_Assignment:UNIT! Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "no-reply@popeco911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
