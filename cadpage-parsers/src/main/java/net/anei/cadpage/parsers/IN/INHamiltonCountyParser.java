package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.GroupBestParser;

public class INHamiltonCountyParser extends GroupBestParser {
  
  public INHamiltonCountyParser() {
    super(new INHamiltonCountyAParser(), new INHamiltonCountyBParser());
  }

}
