package net.anei.cadpage.parsers.DE;
import net.anei.cadpage.parsers.GroupBestParser;



public class DEWilmingtonParser extends GroupBestParser {
  
  public DEWilmingtonParser() {
    super(new DEWilmingtonAParser(), new DEWilmingtonBParser());
  }
} 
