package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.GroupBestParser;


public class LATangipahoaParishParser extends GroupBestParser {
  
  public LATangipahoaParishParser() {
    super(new LATangipahoaParishAParser(), new LATangipahoaParishBParser(),
          new LATangipahoaParishCParser());
  }
}
