package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.GroupBestParser;

public class KSHarveyCountyParser extends GroupBestParser {
  
  public KSHarveyCountyParser() {
    super(new KSHarveyCountyAParser(), new KSHarveyCountyBParser());
  }

}
