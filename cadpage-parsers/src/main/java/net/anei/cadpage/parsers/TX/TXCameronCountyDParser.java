package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class TXCameronCountyDParser extends DispatchH05Parser {

  public TXCameronCountyDParser() {
    super("CAMERON COUNTY", "TX",
          "BOX ADDRCITY DATETIME MAP? CALL UNIT! TIMES+? ST_INFO_BLK INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "Active911@harlingentx.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("BOX")) return new BoxField("\\d{4}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("MAP")) return new MapField("(?:PR)?\\d{4}", true);
    return super.getField(name);
  }

}
