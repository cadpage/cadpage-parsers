package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.MsgParser;

/*
 * Selects best parser from all of the Suffolk County, NY parsers
 */


public class NYSuffolkCountyAllParser extends GroupBestParser {

  public NYSuffolkCountyAllParser() {
    super(new MsgParser[]{
        new NYSuffolkCountyAParser(),
        new NYSuffolkCountyBParser(),
        new NYSuffolkCountyCParser(),
        new NYSuffolkCountyDParser(),
        new NYSuffolkCountyEParser(),
        new NYSuffolkCountyFParser(),
        new NYSuffolkCountyGParser(),
        new NYSuffolkCountyHParser(),
        new NYSuffolkCountyJParser(),
        new NYSuffolkCountyKParser(),
        new NYSuffolkCountyLParser(),
        new NYSuffolkCountyMParser(),
        new NYSuffolkCountyFiretrackerParser(),
        new NYDixHillsParser()
    });
  }

  @Override
  public String getLocName() {
    return "Suffolk County, NY";
  }
}
