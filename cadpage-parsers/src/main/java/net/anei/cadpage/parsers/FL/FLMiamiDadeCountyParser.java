package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.GroupBestParser;



public class FLMiamiDadeCountyParser extends GroupBestParser {
  
  public FLMiamiDadeCountyParser() {
    super(new FLMiamiDadeCountyAParser(), new FLMiamiDadeCountyBParser());
  }
} 
