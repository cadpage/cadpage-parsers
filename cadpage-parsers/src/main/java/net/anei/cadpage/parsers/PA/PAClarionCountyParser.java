package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/*
Clarion County, PA
 */


public class PAClarionCountyParser extends GroupBestParser {
  
  public PAClarionCountyParser() {
    super(new PAClarionCountyFParser(),
          new PAClarionCountyGParser());
  }
  

  static void fixCity(Data data) {
    data.strCity = stripFieldEnd(data.strCity, " BORO");
  }
}
