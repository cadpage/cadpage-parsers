package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

/**
 * Yolo County, CA
 */
public class CAYoloCountyAParser extends DispatchA20Parser {
  
  public CAYoloCountyAParser() {
    super("YOLO COUNTY", "CA", A20_UNIT_LABEL_REQ);
  }
  
  @Override
  public String getFilter() {
    return "@ci.davis.ca.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
}
