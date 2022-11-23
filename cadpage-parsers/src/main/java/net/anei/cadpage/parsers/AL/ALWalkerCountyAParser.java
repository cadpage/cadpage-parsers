package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

/*
Walker County, AL
*/

public class ALWalkerCountyAParser extends DispatchA49Parser {

  public ALWalkerCountyAParser() {
    super("WALKER COUNTY","AL");
  }

  @Override
  public String getFilter() {
    return "@walker911,walker911@";
  }

  @Override
  protected String fixCall(String call) {
    if (call.endsWith(" POLICE DEPARTMENT")) return null;
    if (call.equals("BIRMINGHAM STATE")) return null;
    if (call.equals("REGIONAL PARAMEDICS 9-1-1 CALL")) return null;
    if (call.equals("SHERIFF, UNKNOWN INCIDENT OCCURING")) return null;
    return super.fixCall(call);
  }


}
