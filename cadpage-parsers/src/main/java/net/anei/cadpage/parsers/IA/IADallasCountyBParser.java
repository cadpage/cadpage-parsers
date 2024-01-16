package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class IADallasCountyBParser extends DispatchH05Parser {

  public IADallasCountyBParser() {
    super("DALLAS COUNTY", "IA",
          "CAD_#:ID! Call_Type:CALL! Address:ADDRCITY! Cross_Street:X! Units:UNIT! Lat/Long:GPS! Call_Time:DATETIME! MAP! Narrative:EMPTY! INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "iar@dallascountyiowa.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("MAP")) return new MapField("Districts *(.*)", true);
    return super.getField(name);
  }
}
