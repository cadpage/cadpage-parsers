package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCLenoirCountyParser extends GroupBestParser {
  
  public NCLenoirCountyParser() {
    super(new NCLenoirCountyAParser(), new NCLenoirCountyBParser());
  }
}
