package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXMedinaCountyParser extends DispatchA72Parser {

  public TXMedinaCountyParser() {
    super("MEDINA COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "active911@medinacountytexas.org,medinacodispatch@gmail.com,Active911@Medinatx.org";
  }
}
