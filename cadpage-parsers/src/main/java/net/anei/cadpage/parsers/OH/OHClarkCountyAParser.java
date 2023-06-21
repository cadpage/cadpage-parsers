package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA5Parser;

/**
 * Clark County, OH
 */
public class OHClarkCountyAParser extends DispatchA5Parser {

  public OHClarkCountyAParser() {
    super(CITY_CODES, "CLARK COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "@CI.SPRINGFIELD.OH.US";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CT",             "",                // ????
      "N CARLISLE",     "NEW CARLISLE"
  });
}