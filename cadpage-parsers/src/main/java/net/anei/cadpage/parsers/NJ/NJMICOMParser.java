package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.GroupBestParser;


/*
MICOM, NJ

*/

public class NJMICOMParser extends GroupBestParser {
  
  public NJMICOMParser() {
    super(new NJMICOMAParser(), new NJMICOMBParser());
  }
  
  @Override
  public String getLocName() {
    return "MICCOM (northern NJ), NJ";
  }

}
