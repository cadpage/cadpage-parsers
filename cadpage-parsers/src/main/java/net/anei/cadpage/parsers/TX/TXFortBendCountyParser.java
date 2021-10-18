package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Fort Bend County, TX
 */
public class TXFortBendCountyParser extends GroupBestParser {

  public TXFortBendCountyParser() {
    super(new TXFortBendCountyAParser(),
          new TXFortBendCountyBParser(),
          new TXFortBendCountyCParser());
  }

  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARC", "ARCOLA",
      "BEA", "BEASLEY",
      "BRK", "BROOKSHIRE",
      "DAM", "DAMON",
      "EB",  "EAST BERNARD",
      "FAI", "FAIRCHILDS",
      "FRS", "FRESNO",
      "FUL", "FULSHEAR",
      "GUY", "GUY",
      "HOU", "HOUSTON",
      "KEN", "KENDLETON",
      "KTY", "KATY",
      "MOC", "MISSOURI CITY",
      "MP",  "MEADOWS PLACE",
      "NDV", "NEEDVILLE",
      "ORC", "ORCHARD",
      "PL",  "PEARLAND",
      "PLK", "PLEAK",
      "RIC", "RICHMOND",
      "ROS", "ROSENBERG",
      "RSH", "ROSHARON",
      "SIM", "SIMONTON",
      "STF", "STAFFORD",
      "SUG", "SUGAR LAND",
      "WAL", "WALLIS"
  });
}
