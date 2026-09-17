package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchC11Parser;

public class NCStanlyCountyCParser extends DispatchC11Parser {

  public NCStanlyCountyCParser() {
    this("STANLY COUNTY", "NC");
  }

  public NCStanlyCountyCParser(String defCity, String defState) {
    super(defCity, defState, C11_UNIT);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getAliasCode() {
    return "NCStanlyCounty";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern GRAND_PT_PTN = Pattern.compile("\\bGRAND +PT\\b");

  @Override
  public String adjustGpsLookupAddress(String addr) {
    addr = addr.toUpperCase();
    addr = GRAND_PT_PTN.matcher(addr).replaceAll("GRAND POINT");
    return addr;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "1620 GRAND POINT LANE",                "+35.553814,-81.490755",
  });
}
