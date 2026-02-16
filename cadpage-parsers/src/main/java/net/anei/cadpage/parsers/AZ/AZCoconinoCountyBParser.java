package net.anei.cadpage.parsers.AZ;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA50Parser;

public class AZCoconinoCountyBParser extends DispatchA50Parser {

  public AZCoconinoCountyBParser () {
    super(CITY_CODES, CITY_LIST, "COCONINO COUNTY", "AZ");
  }

  @Override
  public String getFilter() {
    return "ipage@flagstaffaz.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BLUE", "BLUE RIDGE",
      "COCO", "COCONINO COUNTY"
  });

  private static final String[] CITY_LIST = new String[] {
      "HAPPY JACK",
      "SEDONA"
  };

}
