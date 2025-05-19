package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCCumberlandCountyParser extends GroupBestParser {

  public NCCumberlandCountyParser() {
    super(new NCCumberlandCountyAParser(), new NCCumberlandCountyBParser());
  }
}
