package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.GroupBestParser;


public class MDBaltimoreCountyParser extends GroupBestParser {
  
  public MDBaltimoreCountyParser() {
    super(new MDBaltimoreCountyAParser(),
           new MDBaltimoreCountyBParser());
  }
  
}
