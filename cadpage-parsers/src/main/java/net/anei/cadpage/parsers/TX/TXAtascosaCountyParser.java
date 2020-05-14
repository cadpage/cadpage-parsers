package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Atoscosa County, TX
 */
public class TXAtascosaCountyParser extends GroupBestParser {
  
  public TXAtascosaCountyParser() {
    super(new TXAtascosaCountyAParser(), new TXAtascosaCountyBParser());
  }
  
  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BIG", "BIGFOOT",
      "CAM", "CAMPBELLTON",
      "CHA", "CHARLOTTE",
      "CHR", "CHRISTINE",
      "FLO", "FLORESVILLE",
      "JOU", "JOURDANTON",
      "LEM", "LEMING",
      "LYT", "LYTLE",
      "MCC", "MCCOY",
      "NAT", "NATALIA",
      "PLE", "PLEASANTON",
      "POT", "POTEET",
      "SAN", "SAN ANTONIO",
      "SOM", "SOMERSET",
      "VON", "VON ORMY"
  });
}
