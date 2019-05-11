package net.anei.cadpage.parsers.PA;

public class PAUnionCountyParser extends PASnyderCountyBParser {
  
  public PAUnionCountyParser() {
    super("UNION COUNTY");
  }
  
  @Override
  public String getFilter() {
    return "cademail@unionco.org";
  }
}
