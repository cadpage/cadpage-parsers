package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYOwenCountyParser extends GroupBestParser {
  
  public KYOwenCountyParser() {
    super(new KYOwenCountyAParser(), new KYOwenCountyBParser());
  }
}
