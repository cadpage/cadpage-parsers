package net.anei.cadpage.parsers.VT;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Chittenden County, VT
 */
public class VTChittendenCountyParser extends GroupBestParser {
  
  public VTChittendenCountyParser() {
    super(new VTChittendenCountyAParser(), 
          new VTChittendenCountyCParser(),
          new VTChittendenCountyDParser(),
          new VTChittendenCountyEParser(),
          new VTChittendenCountyFParser());
  }
}
