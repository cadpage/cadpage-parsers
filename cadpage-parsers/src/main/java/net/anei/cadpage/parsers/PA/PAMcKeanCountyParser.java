package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
McKean County, PA

 */


public class PAMcKeanCountyParser extends GroupBestParser {

  public PAMcKeanCountyParser() {
    super(new PAMcKeanCountyAParser(),
          new PAMcKeanCountyBParser(),
          new PAMcKeanCountyCParser(),
          new PAMcKeanCountyDParser());
  }
}
