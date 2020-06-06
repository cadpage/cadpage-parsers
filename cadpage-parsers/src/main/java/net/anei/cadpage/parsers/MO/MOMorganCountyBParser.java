package net.anei.cadpage.parsers.MO;

public class MOMorganCountyBParser extends MOMoniteauCountyParser {
  
  public MOMorganCountyBParser() {
    super("MORGAN COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@MORGAN-COUNTY.ORG,DISPATCH@OMNIGO.COM";
  }
}
