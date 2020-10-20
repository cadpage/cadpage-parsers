package net.anei.cadpage.parsers.LA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA66Parser;

public class LAPlaqueminesParishParser extends DispatchA66Parser {
  
  public LAPlaqueminesParishParser() {
    super(CITY_CODES, "PLAQUEMINES PARISH", "LA");
  }
  
  @Override
  public String getFilter() {
    return "ppso.dispatch@ppso.net";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALG", "ALGIERS",
      "ALL", "ALLIANCE",
      "AVO", "AVONDALE",
      "BAT", "BATON ROUGE",
      "BEC", "BECNELVILLE",
      "BEL", "BELLE CHASSE",
      "BOO", "BOOTHVILLE",
      "BRA", "BRAITHWAITE",
      "BUR", "BURAS",
      "CAR", "CARLISLE",
      "CHA", "CHALMETTE",
      "CHV", "CHAUVIN",
      "COV", "COVINGTON",
      "DAV", "DAVANT",
      "DEE", "DEER RANGE",
      "DEN", "DENHAM SPRINGS",
      "DIA", "DIAMOND",
      "EMP", "EMPIRE",
      "GRE", "GRETNA",
      "HAH", "HARAHAN",
      "HAP", "HAPPY JACK",
      "HAR", "HARVEY",
      "HAT", "HATTIESBURG",
      "HOM", "HOMEPLACE",
      "IRO", "IRONTON",
      "JES", "JESUIT BEND",
      "KEN", "KENNER",
      "MAR", "MARRERO",
      "MET", "METAIRIE",
      "MYR", "MYRTLE GROVE",
      "NAR", "NAIRN",
      "NEW", "NEW ORLEANS",
      "NIB", "NEW IBERIA",
      "OAK", "OAKVILLE",
      "PHO", "PHOENIX",
      "PIL", "PILOT TOWN",
      "POI", "PTE-A-LA-HACHE",
      "POR", "PORT SULPHUR",
      "PRT", "PORT EDS",
      "PTC", "POINTE CELESTE",
      "SLI", "SLIDELL",
      "SN",  "SUN",
      "STB", "ST BERNARD",
      "STG", "ST GABRIEL",
      "SUN", "SUNRISE",
      "TER", "TERRYTOWN",
      "THI", "THIBODAUX",
      "TRI", "TRIUMPH",
      "VEN", "VENICE",
      "VIO", "VIOLET",
      "WES", "WESTWEGO",
      "WOO", "WOODLAWN"
  });

}
