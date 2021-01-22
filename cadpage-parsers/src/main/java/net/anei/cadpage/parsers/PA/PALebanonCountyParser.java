package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Lebanon County, PA
 */


public class PALebanonCountyParser extends GroupBestParser {

  public PALebanonCountyParser() {
    super(new PALebanonCountyAParser(), new PALebanonCountyBParser());
  }
}
