package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;


public class MOStLouisCountyParser extends GroupBestParser {

  public MOStLouisCountyParser() {
    super(new MOStLouisCountyAParser(), new MOStLouisCountyBParser(),
        new MOStLouisCountyDParser(),
        new MOStLouisCountyEParser(), new MOStLouisCountyFParser(),
        
        // The C parser is getting to be to promiscuous :(
        new GroupBlockParser(), new MOStLouisCountyCParser());
  }
}
