package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PASusquehannaCountyParser extends GroupBestParser {

  public PASusquehannaCountyParser() {
    super(new PASusquehannaCountyAParser(),
          new PASusquehannaCountyBParser());
  }

}