package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.GroupBestParser;


public class CTNorthwestPublicSafetyParser extends GroupBestParser {
  
  public CTNorthwestPublicSafetyParser() {
    super(new CTNorthwestPublicSafetyAParser(), new CTNorthwestPublicSafetyBParser());
  }
}
