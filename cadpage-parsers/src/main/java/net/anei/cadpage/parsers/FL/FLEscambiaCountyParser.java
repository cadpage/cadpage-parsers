package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLEscambiaCountyParser extends GroupBestParser {
  
  public FLEscambiaCountyParser() {
    super(new FLEscambiaCountyAParser(), new FLEscambiaCountyBParser());
  }
} 
