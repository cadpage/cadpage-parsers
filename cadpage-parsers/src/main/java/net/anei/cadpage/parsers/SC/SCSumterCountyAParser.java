package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class SCSumterCountyAParser extends DispatchA3Parser {
 
  public SCSumterCountyAParser() {
    super(0, "SUMTER COUNTY", "SC");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{3,}", true);
    return super.getField(name);
  }
}
