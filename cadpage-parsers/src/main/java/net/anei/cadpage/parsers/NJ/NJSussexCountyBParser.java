package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA11Parser;

/**
 * Sussex County, NJ
 */
public class NJSussexCountyBParser extends DispatchA11Parser {


  public NJSussexCountyBParser() {
    super(CITY_CODES, "SUSSEX COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "paging@sussexcountysheriff.com,sussexco911@gmail.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "COL", "COLUMBIA",
      "DV",  "DOVER"
  });

}
