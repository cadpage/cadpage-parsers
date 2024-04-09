package net.anei.cadpage.parsers.GA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class GASchleyCountyAParser extends DispatchA19Parser {

  public GASchleyCountyAParser() {
    this("SCHLEY COUNTY");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  public GASchleyCountyAParser(String defCounty) {
    super(defCounty, "GA");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com,middleflint@mfre911.com,dispatch@mfre911.com";
  }

  @Override
  public String getAliasCode() {
    return "GASchleyCounty";
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "568 HWY 280 W",                        "+32.056300,-84.288500"
  });
}
