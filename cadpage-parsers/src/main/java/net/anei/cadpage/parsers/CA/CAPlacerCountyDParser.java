package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA84Parser;

public class CAPlacerCountyDParser extends DispatchA84Parser {

  public CAPlacerCountyDParser() {
    super(CITY_CODES, "PLACER COUNTY", "CA");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AP", "APPLEGATE",
      "CO", "COLFAX",
      "FH", "FORESTHILL",
      "GB", "GRANITE BAY",
      "LO", "LOOMIS",
      "NC", "NEWCASTLE"
  });
}
