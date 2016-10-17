package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

/*
Walker County, AL
*/

public class ALWalkerCountyParser extends DispatchA49Parser {

  public ALWalkerCountyParser() {
    super("WALKER COUNTY","AL");
  }
  
  @Override
  public String getFilter() {
    return "@walker911,walker911@";
  }
}
