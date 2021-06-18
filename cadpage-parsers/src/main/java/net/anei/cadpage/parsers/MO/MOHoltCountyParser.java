package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOHoltCountyParser extends DispatchA33Parser {

  public MOHoltCountyParser() {
    super("HOLT COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "NOREPLY@OMNIGO.COM";
  }
}
