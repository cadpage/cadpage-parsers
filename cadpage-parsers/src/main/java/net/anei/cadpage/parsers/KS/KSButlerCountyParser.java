package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.GroupBestParser;

public class KSButlerCountyParser extends GroupBestParser {
  
  public KSButlerCountyParser() {
    super(new KSButlerCountyAParser(), new KSButlerCountyBParser(),
          new KSButlerCountyCParser());
  }

}
