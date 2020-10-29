package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MSWinstonCountyParser extends DispatchA48Parser {
  
  public MSWinstonCountyParser() {
    super(CITY_LIST, "WINSTON COUNTY", "MS", FieldType.X, A48_NO_CODE);
  }

  @Override
  public String getFilter() {
    return "@bellsouth.net";
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "LOUISVILLE",

      // Town
      "NOXAPATER",

      // Unincorporated communities
      "HIGHPOINT",
      "VERNON",

      // Ghost towns
      "PERKINSVILLE",
      "RANDALLS BLUFF",
      "SINGLETON"
  };
}
