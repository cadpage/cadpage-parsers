package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHDelawareCountyBParser extends DispatchH05Parser {

  public OHDelawareCountyBParser() {
    super("DELAWARE COUNTY", "OH",
          "SEQ DATETIME UNIT PLACE ADDRCITY X INFO_BLK+? TIMES+? ID!");
  }

  @Override
  public String getFilter() {
    return "del-911@co.delaware.oh.us";
  }

  @Override
  public Field getField(String name) {
    return super.getField(name);
  }

}
