package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.GroupBestParser;

public class MNMinneapolisStPaulParser extends GroupBestParser {
  
  public MNMinneapolisStPaulParser() {
    super(new MNMinneapolisStPaulAParser(), new MNMinneapolisStPaulBParser());
  }
} 