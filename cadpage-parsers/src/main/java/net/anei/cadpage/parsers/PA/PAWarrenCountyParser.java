package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PAWarrenCountyParser extends GroupBestParser {

  public PAWarrenCountyParser() {
    super(new PAWarrenCountyAParser(),
          new PAWarrenCountyBParser(),
          new PAWarrenCountyCParser());
  }

}