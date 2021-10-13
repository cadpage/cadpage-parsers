package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNCrockettCountyParser extends DispatchA74Parser {

  public TNCrockettCountyParser() {
    super("CROCKETT COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm3.info";
  }
}
