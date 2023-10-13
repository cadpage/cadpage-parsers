package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Franklin County, PA
 */


public class PAFranklinCountyParser extends GroupBestParser {

  public PAFranklinCountyParser() {
    super(new PAFranklinCountyAParser(),
          new PAFranklinCountyBParser(),
          new PAFranklinCountyCParser(),
          new PAFranklinCountyDParser(),
          new PAFranklinCountyEParser());
  }
}
