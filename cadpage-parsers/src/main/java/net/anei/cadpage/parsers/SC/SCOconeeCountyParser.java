package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCOconeeCountyParser extends GroupBestParser {
  
  public SCOconeeCountyParser() {
    super(new SCOconeeCountyAParser(), new SCOconeeCountyBParser(),
          new SCOconeeCountyCParser());
  }
}