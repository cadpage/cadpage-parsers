package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.GroupBestParser;

public class IDBinghamCountyParser extends GroupBestParser {

  public IDBinghamCountyParser() {
   super(new IDBinghamCountyAParser(), new IDBinghamCountyBParser());
  }
}
