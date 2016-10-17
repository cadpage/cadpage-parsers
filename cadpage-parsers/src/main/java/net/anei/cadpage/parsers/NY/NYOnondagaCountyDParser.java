package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchRedAlertParser;


public class NYOnondagaCountyDParser extends DispatchRedAlertParser {

  public NYOnondagaCountyDParser() {
    super("ONONDAGA COUNTY", "NY");
    setupMultiWordStreets(
        "RANSOM MACKENZIE",
        "SUMMERFIELD VILLAGE"
    );
  }
}
	