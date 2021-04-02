package net.anei.cadpage.parsers.WA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WAKlickitatCountyParser extends DispatchA19Parser {

  public WAKlickitatCountyParser() {
    super(CITY_CODES, "KLICKITAT COUNTY", "WA");
   }

  @Override
  public String getFilter() {
    return "@alert.active911.com,noreply@klickitatcounty.org";
  }

  private static final Properties CITY_CODES =  buildCodeTable(new String[] {
      "APP", "APPLETON",
      "BIN", "BINGEN",
      "BZ",  "BZ CORNERS",
      "CEN", "CENTERVILLE",
      "DAL", "DALLESPORT",
      "GLD", "GOLDENDALE",
      "GLN", "GLENWOOD",
      "HPR", "HIGH PRARIE",
      "KLI", "KLICKITAT",
      "LYL", "LYLE",
      "MAR", "MARRYHILL",
      "RSV", "ROOSEVELT",
      "SNO", "SNOWDEN",
      "TIM", "TIMBER VALLEY",
      "TL",  "TROUT LAKE",
      "WHK", "WAHKIACUS",
      "WS",  "WHITE SALMON",
      "WSH", "WISHRAM"

  });
}
