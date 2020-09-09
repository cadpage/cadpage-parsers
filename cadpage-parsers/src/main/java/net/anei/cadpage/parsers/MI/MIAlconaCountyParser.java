package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.GroupBestParser;

public class MIAlconaCountyParser extends GroupBestParser {
  
  public MIAlconaCountyParser() {
    super(new MIAlconaCountyAParser(), new MIAlconaCountyBParser());
  }
} 