package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;

/*
 * Klamath County, OR
 */

public class ORKlamathCountyParser extends DispatchA22Parser {
  
  public ORKlamathCountyParser() {
    super(CITY_CODES, "KLAMATH COUNTY", "OR");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "kc-911@kc911.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected String adjustGpsLookupAddress(String addr) {
    addr = TRAIL_DIR_PTN.matcher(addr).replaceFirst("");
    addr = MPNN_PTN.matcher(addr).replaceAll("MP $1 ");
    return addr;
  }

  private static final Pattern TRAIL_DIR_PTN = Pattern.compile(" +[NSEW]{1,2}$");
  private static final Pattern MPNN_PTN = Pattern.compile("MP(\\d+) ");
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "MP 32 HWY 140",                        "+42.395990,-122.293210",
      "MP 33 HWY 140",                        "+42.392960,-122.273920",
      "MP 34 HWY 140",                        "+42.389720,-122.255080",
      "MP 35 HWY 140",                        "+42.384440,-122.237920",
      "MP 36 HWY 140",                        "+42.392120,-122.222490",
      "MP 37 HWY 140",                        "+42.395920,-122.204250",
      "MP 38 HWY 140",                        "+42.400360,-122.186680",
      "MP 39 HWY 140",                        "+42.411230,-122.176060",
      "MP 40 HWY 140",                        "+42.425700,-122.173860",
      "MP 41 HWY 140",                        "+42.431890,-122.158870",
      "MP 42 HWY 140",                        "+42.434190,-122.139880",
      "MP 43 HWY 140",                        "+42.437200,-122.120150",
      "MP 44 HWY 140",                        "+42.449410,-122.112050",
      "MP 45 HWY 140",                        "+42.442950,-122.096410",
      "MP 46 HWY 140",                        "+42.433090,-122.082370",
      "MP 47 HWY 140",                        "+42.421900,-122.069400",
      "MP 48 HWY 140",                        "+42.410680,-122.057040",
      "MP 49 HWY 140",                        "+42.401120,-122.042500",
      "MP 50 HWY 140",                        "+42.392210,-122.026620",
      "MP 51 HWY 140",                        "+42.383320,-122.010830",
      "MP 52 HWY 140",                        "+42.373230,-121.998270",
      "MP 53 HWY 140",                        "+42.359230,-121.990890",
      "MP 54 HWY 140",                        "+42.348390,-121.977800"
  });

  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "KF", "KLAMATH FALLS"
  });
}
