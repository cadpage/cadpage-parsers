package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;

/**
Tulsa, OK

 */
public class OKTulsaParser extends GroupBestParser {
  public OKTulsaParser() {
    super(new OKTulsaAParser(), new OKTulsaBParser());
  }
}
