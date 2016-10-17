package net.anei.cadpage.parsers.NY;


import net.anei.cadpage.parsers.dispatch.DispatchRedAlertParser;


public class NYRocklandCountyAParser extends DispatchRedAlertParser {
  
  public NYRocklandCountyAParser() {
    super("ROCKLAND COUNTY","NY");
    setupMultiWordStreets(
        "BUENA VISTA",
        "ELINOR PL SCHRIEVER",
        "FAIR HAVEN",
        "PHILLIPS HILL"
    );
  }
}
