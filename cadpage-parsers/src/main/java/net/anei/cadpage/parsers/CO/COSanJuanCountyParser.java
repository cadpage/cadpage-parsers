package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class COSanJuanCountyParser extends DispatchH03Parser {

  public COSanJuanCountyParser() {
    super("SAN JUAN COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "@CSP.CAD";
  }
}
