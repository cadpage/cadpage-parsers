package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLLakeCountyParser extends GroupBestParser {
  
  public FLLakeCountyParser() {
    super(new FLLakeCountyAParser(), new FLLakeCountyBParser());
  }
} 
