package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.GroupBestParser;

public class IDBlaineCountyParser extends GroupBestParser {

  public IDBlaineCountyParser() {
   super(new IDBlaineCountyAParser(), new IDBlaineCountyBParser());
  }
}
