package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCLancasterCountyParser extends GroupBestParser {
  
  public SCLancasterCountyParser() {
    super(new SCLancasterCountyAParser(), new SCLancasterCountyBParser(),
          new SCLancasterCountyCParser());
  }
}