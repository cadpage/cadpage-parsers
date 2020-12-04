package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;

public class CASanLuisObispoCountyCParser extends DispatchA22Parser {

  public CASanLuisObispoCountyCParser() {
    super(CITY_CODES, "SAN LUIS OBISPO COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "Disptach@atascadero.org";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AT", "ATASCADERO"
  });

}
