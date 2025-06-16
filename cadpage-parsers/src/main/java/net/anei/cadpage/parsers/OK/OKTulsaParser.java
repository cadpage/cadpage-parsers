package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.GroupBlockParser;

/**
Tulsa, OK

 */
public class OKTulsaParser extends GroupBestParser {
  public OKTulsaParser() {
    super(new OKTulsaAParser(),
          new OKTulsaCParser(),
          new GroupBlockParser(),
          new OKTulsaBParser());
  }
}
