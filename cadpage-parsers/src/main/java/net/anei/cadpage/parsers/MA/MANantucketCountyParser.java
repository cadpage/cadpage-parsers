package net.anei.cadpage.parsers.MA;

import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;

public class MANantucketCountyParser extends DispatchA63Parser {
  public MANantucketCountyParser() {
    super(CITY_LIST, "NANTUCKET COUNTY", "MA");
  }

  @Override
  public String getFilter() {
    return "CADNotify@nantucketpolice.com";
  }

  private static final String[] CITY_LIST = new String[] {
      "MADAKET",
      "MIACOMET",
      "NANTUCKET",
      "POLPIS",
      "SCONSET",
      "SIASCONSET",
      "SURFSIDE",
      "WAUWINET"
  };
}
