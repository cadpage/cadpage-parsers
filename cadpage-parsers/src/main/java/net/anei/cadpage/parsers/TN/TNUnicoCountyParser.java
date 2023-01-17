package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNUnicoCountyParser extends DispatchA74Parser {

  public TNUnicoCountyParser() {
    super("UNICO COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@unicoitne911.info";
  }

}
