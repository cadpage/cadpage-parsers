package net.anei.cadpage.parsers.IL;


public class ILCookCountyEParser extends ILDuPageCountyCParser {
  
  public ILCookCountyEParser() {
    super("COOK COUNTY", "IL");
  }
  
  @Override
  public String getFilter() {
    return "911@nwcds.org";
  }
}
