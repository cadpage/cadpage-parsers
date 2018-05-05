package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.GroupBestParser;

public class KSMarionCountyParser extends GroupBestParser {
  
  public KSMarionCountyParser() {
    super(new KSMarionCountyAParser(), new KSMarionCountyBParser());
  }

}
