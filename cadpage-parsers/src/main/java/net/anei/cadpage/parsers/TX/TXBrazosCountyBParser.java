package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA84Parser;

public class TXBrazosCountyBParser extends DispatchA84Parser {

  public TXBrazosCountyBParser() {
    super(CITY_CODES, "BRAZOS COUNTY", "TX");
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BC", "BRAZOS COUNTY",
      "BR", "BRYAN"
  });
}
