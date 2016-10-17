package net.anei.cadpage.parsers.NV;

import net.anei.cadpage.parsers.GroupBestParser;



public class NVElkoCountyParser extends GroupBestParser {
  
  public NVElkoCountyParser() {
    super(new NVElkoCountyAParser(), new NVElkoCountyBParser());
  }
}
