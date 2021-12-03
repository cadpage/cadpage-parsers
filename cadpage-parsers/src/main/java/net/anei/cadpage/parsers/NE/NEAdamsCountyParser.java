package net.anei.cadpage.parsers.NE;

import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;

public class NEAdamsCountyParser extends DispatchA38Parser {

  public NEAdamsCountyParser() {
    super("ADAMS COUNTY", "NE");
  }

  @Override
  public String getFilter() {
    return "TAC10Email@hastingspolice.org";
  }

}
