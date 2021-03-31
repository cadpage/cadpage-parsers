package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class MIStJosephCountyParser extends DispatchA1Parser {

  public MIStJosephCountyParser() {
    super("ST JOSEPH COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "centrald@stjosephcountymi.org";
  }
}
