package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ILJacksonCountyBParser extends DispatchH05Parser {

  public ILJacksonCountyBParser() {
    super("JACKSON COUNTY", "IL",
          "ID DATETIME ADDRCITY INFO_BLK/Z+? SRC GPS1 GPS2! https:SKIP! TIMES+? UNIT! END");
  }

  @Override
  public String getFilter() {
    return "avafire@applecityfd.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,4}[FP]D|MPS", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
