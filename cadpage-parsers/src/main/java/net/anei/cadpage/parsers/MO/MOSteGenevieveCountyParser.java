package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Ste. Genevieve County, MO
 */
public class MOSteGenevieveCountyParser extends DispatchCiscoParser {
  
  public MOSteGenevieveCountyParser() {
    super("STE GENEVIEVE COUNTY", "MO");
    removeWords("STE");
    setupMultiWordStreets(
        "LIME KILN",
        "STE GENEVIEVE",
        "WHITE SANDS");
  }

  @Override
  public String getFilter() {
    return "sfc_cad@sfc911.org";
  }
}
  