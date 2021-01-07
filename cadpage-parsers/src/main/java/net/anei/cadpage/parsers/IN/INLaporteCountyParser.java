package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INLaporteCountyParser extends DispatchSPKParser {
  public INLaporteCountyParser() {
    super(CITY_CODES, "LAPORTE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "interact@lcso.in.gov";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "KINGSFORD HTS",    "KINGSFORD HEIGHTS",
      "MCCY",             "MICHIGAN CITY",
      "LACROSSE",         "LA CROSSE",
      "ROLNG PRAIRIE",    "ROLLING PRAIRIE"
  });
}