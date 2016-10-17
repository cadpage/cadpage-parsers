package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILPeoriaCountyParser extends GroupBestParser {
  
  public ILPeoriaCountyParser() {
    super(new ILPeoriaCountyAParser(), new ILPeoriaCountyBParser());

  }
}
