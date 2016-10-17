package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDCecilCountyParser extends GroupBestParser {
  
  public MDCecilCountyParser() {
    super(new MDCecilCountyAParser(),
           new MDCecilCountyBParser(),
           new MDCecilCountyCParser(),
           new MDCecilCountyDParser(),
           new MDCecilCountyEParser());
  }
  
}
