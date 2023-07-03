package net.anei.cadpage.parsers.WI;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;



public class WIMilwaukeeCountyParser extends DispatchA63Parser {
  public WIMilwaukeeCountyParser() {
    super(CITY_CODES, "MILWAUKEE COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return "phoenix@bayside-wi.gov";
  }


  private static final Properties CITY_CODES = buildCodeTable (new String[]{
      "BA", "BAYSIDE",
      "FD", "MILWAKEE",
      "GL", "GLENDALE",
      "WF", "WHITEFISH BAY",
      "RV", "RIVER HILLS",
      "BD", "BROWN DEER",
      "SH", "SHOREWOOD",
      "FP", "FOX POINT",
  });
}
