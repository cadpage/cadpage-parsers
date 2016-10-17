package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Fort Bend County, TX
 */
public class TXFortBendCountyParser extends GroupBestParser {
  
  public TXFortBendCountyParser() {
    super(new TXFortBendCountyAParser(), new TXFortBendCountyBParser());
  }
  
  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BEA", "BEASLEY",
      "EB", "EAST BERNARD", 
      "FUL", "FULSHEAR",
      "HOU", "HOUSTON",
      "KEN", "KENDLETON",
      "KTY", "KATY",
      "NDV", "NEEDVILLE",
      "PLK", "PLEAK",
      "RIC", "RICHMOND",
      "ROS", "ROSENBERG",
      "SIM", "SIMONTON",
      "SUG", "SUGAR LAND",
      "WAL", "WALLIS",
  });

}
