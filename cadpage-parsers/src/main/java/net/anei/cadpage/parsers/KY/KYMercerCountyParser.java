package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class KYMercerCountyParser extends DispatchA24Parser {

  public KYMercerCountyParser() {
    super("MERCER COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com,noreply@10-8systems.com";
  }

}
