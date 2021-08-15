package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCLexingtonCountyParser extends GroupBestParser {

  public SCLexingtonCountyParser() {
    super(new SCLexingtonCountyAParser(), new SCLexingtonCountyBParser());
  }
}