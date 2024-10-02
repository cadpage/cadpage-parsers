package net.anei.cadpage.parsers.AL;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

/**
 * St Clair County, AL
 */
public class ALStClairCountyBParser extends DispatchA71Parser {

  public ALStClairCountyBParser() {
    super("ST CLAIR COUNTY", "AL");
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "DAVIS LAKE",    "SPRINGFIELD"
  });
}
