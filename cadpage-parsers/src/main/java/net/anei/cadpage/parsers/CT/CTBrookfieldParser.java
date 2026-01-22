package net.anei.cadpage.parsers.CT;


import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class CTBrookfieldParser extends DispatchA32Parser {

  public CTBrookfieldParser() {
    super(CITY_LIST, "BROOKFIELD","CT");
  }

  @Override
  public String getFilter() {
    return "policedispatch@brookfieldct.gov,BROOKFIELDCTDISPATCH@GMAIL.COM,brookfieldctdispatch2@gmail.com";
  }

  private static final String[] CITY_LIST = new String[]{
    "BRIDGEWATER",
    "BROOKFIELD",
    "DANBURY",
    "MILFORD",
    "ROXBURY"
  };
}
