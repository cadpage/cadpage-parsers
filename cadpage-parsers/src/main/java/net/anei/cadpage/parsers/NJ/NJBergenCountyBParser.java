package net.anei.cadpage.parsers.NJ;

public class NJBergenCountyBParser extends NJSussexCountyAParser {
  
  
  public NJBergenCountyBParser() {
    super("BERGEN COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "anyone@ParamusPolice.org";
  }
}
