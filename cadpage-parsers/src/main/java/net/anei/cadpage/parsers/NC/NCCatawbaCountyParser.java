package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCCatawbaCountyParser extends GroupBestParser {

  public NCCatawbaCountyParser() {
    super(new NCCatawbaCountyAParser(), new NCCatawbaCountyBParser());
  }
}
