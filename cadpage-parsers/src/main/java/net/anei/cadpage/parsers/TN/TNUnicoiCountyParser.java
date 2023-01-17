package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNUnicoiCountyParser extends DispatchA74Parser {

  public TNUnicoiCountyParser() {
    super("UNICOI COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@unicoitne911.info";
  }

}
