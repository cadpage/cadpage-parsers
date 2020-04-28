package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;


public class NCWataugaCountyParser extends GroupBestParser {
  
  public NCWataugaCountyParser() {
    super(new NCWataugaCountyBParser(),
          new NCWataugaCountyAParser());
  }
}
