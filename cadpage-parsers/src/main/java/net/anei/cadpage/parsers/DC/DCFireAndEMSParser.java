package net.anei.cadpage.parsers.DC;

import net.anei.cadpage.parsers.GroupBestParser;


public class DCFireAndEMSParser extends GroupBestParser {

  public DCFireAndEMSParser() {
    super(new DCFireAndEMSAParser(), new DCFireAndEMSBParser());
  }
}
