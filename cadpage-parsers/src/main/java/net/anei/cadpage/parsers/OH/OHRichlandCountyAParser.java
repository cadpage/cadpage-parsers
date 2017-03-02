package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchH02Parser;

public class OHRichlandCountyAParser extends DispatchH02Parser {
  
  public OHRichlandCountyAParser() {
    super(CITY_CODES, "RICHLAND COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "911-DISPATCHERS@RICHLANDCOUNTYOH.US";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BEL", "BELLVILLE",
      "LUC", "LUCAS",
      "MANS", "MANSFIELD",
      "PERR", "PERRY"
  });
}
