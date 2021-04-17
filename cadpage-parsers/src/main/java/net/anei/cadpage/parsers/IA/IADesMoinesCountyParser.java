package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.GroupBestParser;

public class IADesMoinesCountyParser extends GroupBestParser {

  public IADesMoinesCountyParser() {
   super(new IADesMoinesCountyAParser(), new IADesMoinesCountyBParser());
  }
}
