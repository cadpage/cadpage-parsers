package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.GroupBestParser;


public class SCSpartanburgCountyParser extends GroupBestParser {

  public SCSpartanburgCountyParser() {
    super(new SCSpartanburgCountyBParser(), new SCSpartanburgCountyCParser());
  }
}