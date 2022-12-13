package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Juniata County, PA
 */


public class PAJuniataCountyParser extends GroupBestParser {

  public PAJuniataCountyParser() {
    super(new PAJuniataCountyBParser(),
          new PAJuniataCountyCParser());
  }
}
