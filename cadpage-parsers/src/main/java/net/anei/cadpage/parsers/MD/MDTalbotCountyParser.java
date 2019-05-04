package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDTalbotCountyParser extends GroupBestParser {
 
  public MDTalbotCountyParser() {
    super(new MDTalbotCountyAParser(), 
          new MDTalbotCountyBParser(),
          new MDTalbotCountyCParser());
  }
}
