package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.GroupBestParser;

public class PAYorkCountyParser extends GroupBestParser {
  
  public PAYorkCountyParser() {
    super(new PAYorkCountyAParser(), 
          new PAYorkCountyBParser(), 
          new PAYorkCountyCParser(),
          new PAYorkCountyDParser());
  }
  
}