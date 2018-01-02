package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCOrangeburgCountyParser extends GroupBestParser {
  
  public SCOrangeburgCountyParser() {
    super(new SCOrangeburgCountyAParser(), new SCOrangeburgCountyBParser(),
          new SCOrangeburgCountyCParser());
  }
}