package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILLakeCountyParser extends GroupBestParser {

  public ILLakeCountyParser() {
    super(new ILLakeCountyAParser(),
          new ILLakeCountyBParser(),
          new ILLakeCountyDParser(),
          new ILLakeCountyEParser(),
          new ILLakeCountyFParser(),
          new ILLakeCountyGParser());

  }
}
