package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;

public class MDPrinceGeorgesCountyParser extends GroupBestParser {
  
  public MDPrinceGeorgesCountyParser() {
    super(new MDPrinceGeorgesCountyFireBizParser(), 
          new MDPrinceGeorgesCountyDParser(),
          new MDPrinceGeorgesCountyEParser(),
          new MDPrinceGeorgesCountyFParser(),
          new MDPrinceGeorgesCountyGParser(),
          new MDPrinceGeorgesCountyHParser());
  }
}
