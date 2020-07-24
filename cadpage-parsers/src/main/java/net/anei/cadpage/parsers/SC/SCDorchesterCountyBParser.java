package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class SCDorchesterCountyBParser extends DispatchA19Parser {

  public SCDorchesterCountyBParser() {
    super("DORCHESTER COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "spdnotify@summervillesc.gov,sds@smvpd-spillman.police.local";
  }

}
