package net.anei.cadpage.parsers.WY;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class WYGoshenCountyParser extends DispatchA20Parser {

  public WYGoshenCountyParser() {
    super("GOSHEN COUNTY", "WY");
  }

  @Override
  public String getFilter() {
    return "@torringtonpolice.org,paging@goshencounty.org";
  }
}
