package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class KYHendersonCountyAParser extends DispatchA24Parser {

  public KYHendersonCountyAParser() {
    super("HENDERSON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }
}
