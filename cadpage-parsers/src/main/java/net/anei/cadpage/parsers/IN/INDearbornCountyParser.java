package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INDearbornCountyParser extends DispatchA19Parser {

  public INDearbornCountyParser() {
    super(CITY_CODES, "DEARBORN COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "LAW", "LAWRENCEBURG"
  });

}
