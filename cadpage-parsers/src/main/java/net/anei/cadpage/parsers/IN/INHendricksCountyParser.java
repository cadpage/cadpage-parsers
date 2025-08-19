package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INHendricksCountyParser extends GroupBestParser {

  public INHendricksCountyParser() {
    super(new INHendricksCountyAParser(), new INHendricksCountyBParser());
  }

}
