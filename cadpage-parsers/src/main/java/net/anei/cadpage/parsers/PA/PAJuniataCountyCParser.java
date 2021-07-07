package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class PAJuniataCountyCParser extends DispatchH06Parser {

  public PAJuniataCountyCParser() {
    super("JUNIATA COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "noreply@jcpc911.com";
  }
}
