package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INStJosephCountyCParser extends DispatchA19Parser {

  public INStJosephCountyCParser() {
    super(CITY_CODES, "ST JOSEPH COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "noreply@nd.edu";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ND",  "NOTRE DAME",
      "SB",  "SOUTH BEND",
      "SJC", "ST JOSEPH COUNTY"
  });

}
