package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCChathamCountyParser extends GroupBestParser {
  
  public NCChathamCountyParser() {
    super(new NCChathamCountyAParser(), new NCChathamCountyBParser());
  }
}
