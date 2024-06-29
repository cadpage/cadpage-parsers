package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCRichlandCountyParser extends GroupBestParser {

  public SCRichlandCountyParser() {
    super(new SCRichlandCountyAParser(), new SCRichlandCountyBParser());
  }
}