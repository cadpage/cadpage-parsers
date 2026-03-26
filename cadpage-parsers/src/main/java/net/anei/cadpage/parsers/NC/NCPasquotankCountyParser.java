package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.GroupBestParser;


public class NCPasquotankCountyParser extends GroupBestParser {

  public NCPasquotankCountyParser() {
    super(new NCPasquotankCountyAParser(), new NCPasquotankCountyBParser());
  }
}
