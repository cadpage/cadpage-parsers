package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;


public class NCStanlyCountyParser extends GroupBestParser {
  
  public NCStanlyCountyParser() {
    super(new NCStanlyCountyBParser(),
          new GroupBlockParser(),
          new NCStanlyCountyAParser());
  }
}
