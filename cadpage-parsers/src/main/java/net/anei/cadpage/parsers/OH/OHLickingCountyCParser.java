package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHLickingCountyCParser extends DispatchH05Parser {

  public OHLickingCountyCParser() {
    super("LICKING COUNTY", "OH",
          "Title:CALL! Place:PLACE! Address:ADDRCITY! Unit:UNIT! Latitude:GPS1! Longitude:GPS2! Cross_Street:X! " +
            "Units:UNIT/CS! CAD_Code:CODE! Map_Code:MAP! Priority:PRI! Notes:EMPTY INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "@lickingcounty.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
