package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILDuPageCountyParser extends GroupBestParser {
  
  public ILDuPageCountyParser() {
    super(new ILDuPageCountyAParser(), new ILDuPageCountyBParser(), new ILDuPageCountyCParser());
  }

}
