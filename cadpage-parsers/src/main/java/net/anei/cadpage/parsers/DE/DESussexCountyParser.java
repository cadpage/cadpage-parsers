package net.anei.cadpage.parsers.DE;
import net.anei.cadpage.parsers.GroupBestParser;



public class DESussexCountyParser extends GroupBestParser {
  
  public DESussexCountyParser() {
    super(new DESussexCountyAParser(), new DESussexCountyBParser());
  }
} 
