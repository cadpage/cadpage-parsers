package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;


public class NCMecklenburgCountyParser extends GroupBestParser {
  
  public NCMecklenburgCountyParser() {
    super(new NCMecklenburgCountyAParser(), new GroupBlockParser(), new NCMecklenburgCountyBParser());
  }
}
