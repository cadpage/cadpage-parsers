package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class OKSequoyahCountyParser extends DispatchProQAParser {
  
  public OKSequoyahCountyParser() {
    super("SEQUOYAH COUNTY", "OK", 
         "ID! CALL CALL/L+? ADDR/Z APT EMPTY/Z CITY! UNKNOWN! INFO/N+", true);
  }
  
  @Override
  public String getFilter() {
    return "911notify@alternate3.itpartnergroup.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNKNOWN")) return new SkipField("<Unknown>", true);
    return super.getField(name);
  }
}
