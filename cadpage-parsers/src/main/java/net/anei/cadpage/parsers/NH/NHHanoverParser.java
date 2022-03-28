package net.anei.cadpage.parsers.NH;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Hanover, NH
*/


public class NHHanoverParser extends GroupBestParser {

  public NHHanoverParser() {
    super(new NHHanoverAParser(), new NHHanoverBParser());
  }
}
