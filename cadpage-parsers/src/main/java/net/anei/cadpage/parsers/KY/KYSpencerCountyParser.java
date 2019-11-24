package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.GroupBestParser;


public class KYSpencerCountyParser extends GroupBestParser {
  
  public KYSpencerCountyParser() {
    super(new KYSpencerCountyAParser(), new KYSpencerCountyBParser());
  }
}
