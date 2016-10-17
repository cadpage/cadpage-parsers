package net.anei.cadpage.parsers.IN;

public class INHancockCountyBParser extends INShelbyCountyAParser {
  
  public INHancockCountyBParser() {
    super("HANCOCK COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "interact@hancockcoingov.org";
  }
}
