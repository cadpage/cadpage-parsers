package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;


public class MIInghamCountyParser extends GroupBestParser {
  
  public MIInghamCountyParser() {
    super(new MIInghamCountyAParser(), new MIInghamCountyBParser(),
        new MIInghamCountyCParser());
  }
  
}
