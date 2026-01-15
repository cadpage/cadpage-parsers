package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYRockCastleCountyParser extends GroupBestParser {

  public KYRockCastleCountyParser() {
    super(new KYRockCastleCountyAParser(), new KYRockCastleCountyBParser());
  }
}
