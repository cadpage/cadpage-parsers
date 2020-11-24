package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CAMonoCountyParser extends DispatchA20Parser {

  public CAMonoCountyParser() {
    super("MONO COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "@MONOSHERIFF.ORG";
  }

}
