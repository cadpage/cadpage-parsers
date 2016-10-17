package net.anei.cadpage.parsers.DE;

public class DESussexCountyAParser extends DEKentCountyBParser {
  
  public DESussexCountyAParser() {
    super("SUSSEX COUNTY", "DE");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_REMOVE_EXT;
  }
}
