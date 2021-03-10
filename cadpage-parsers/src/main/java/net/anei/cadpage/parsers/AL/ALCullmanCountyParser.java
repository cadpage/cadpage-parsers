package net.anei.cadpage.parsers.AL;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;
/**
 * Pelham, AL
 */
public class ALCullmanCountyParser extends DispatchA19Parser {

  public ALCullmanCountyParser() {
    super(CITY_CODES, "CULLMAN COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com,donotreply@cullmansheriff.org";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AD",  "ADDISON",
      "AL1", "ALBERTVILLE",
      "AL2", "ALBERTVILLE",
      "AR",  "ARAB",
      "AS",  "ASHVILLE",
      "AT",  "ALTOONA",
      "AY",  "ARLEY",
      "BA",  "BAILEYTON",
      "BE",  "BERLIN",
      "BL",  "BLOUNTSVILLE",
      "BO1", "BOAZ",
      "BO2", "BOAZ",
      "BR",  "BREMEN",
      "CA",  "CARBON HILL",
      "CL",  "CLEVELAND",
      "CR",  "CRANE HILL",
      "CU",  "CULLMAN",
      "CU1", "CULLMAN",
      "CU2", "CULLMAN",
      "CU3", "CULLMAN",
      "CY",  "COLONY",
      "DA",  "DAYVILLE",
      "DC",  "DODGE CITY",
      "DE1", "DECATUR",
      "DE2", "DECATUR",
      "DO",  "DORA",
      "DS",  "DOUBLE SPRINGS",
      "EM",  "EMPIRE",
      "EV",  "EVA",
      "FA",  "FALKVILLE",
      "FP",  "FORT PAYNE",
      "FV",  "FAIRVIEW",
      "GA",  "GALLANT",
      "GC",  "GARDEN CITY",
      "GD",  "GARDENDALE",
      "GH",  "GOOD HOPE",
      "GR",  "GRANT",
      "GU",  "GUNTERSVILLE",
      "HA",  "HANCEVILLE",
      "HI",  "HILLSBORO",
      "HN",  "HANCEVILLE",
      "HO",  "HOLLY POND",
      "HP",  "HOLLY POND",
      "HT",  "HUSTON",
      "HU1", "HUNTSVILLE",
      "HU2", "HUNSTVILLE",
      "HV",  "HANCEVILLE",
      "JA1", "JASPER",
      "JA2", "JASPER",
      "JA3", "JASPER",
      "JO",  "JOPPA",
      "KI",  "KIMBERLY",
      "LN",  "LOGAN",
      "LO",  "LOCUST FORK",
      "LS",  "LACEY SPRINGS",
      "MA",  "MADISON",
      "MH",  "MOUNT HOPE",
      "MN",  "MOULTON",
      "MO",  "MORRIS",
      "NA",  "NAUVOO",
      "NH",  "NEW HOPE",
      "OA",  "OAKMAN",
      "OD",  "ODENVILLE",
      "ON",  "ONEONTA",
      "OW",  "OWENS CORSS RDS",
      "PA",  "PARRISH",
      "PS",  "PINSON",
      "RA",  "RAGLAND",
      "RE",  "REMLAP",
      "SAR", "SARDIS",
      "SC",  "SCOTTSBORO",
      "SO",  "SOMERVILLE",
      "SP",  "SPRINGVILLE",
      "ST",  "STEELE",
      "SU",  "SUMITON",
      "TC",  "TOWN CREEK",
      "TO",  "TOWNLEY",
      "TR",  "TRAFFORD",
      "TY",  "TRINITY",
      "UG",  "UNION GROVE",
      "VI",  "VINEMONT",
      "VS",  "VALHERMSO SPGS",
      "WA",  "WARRIOR",
      "WO",  "WOODENVILLE",
      "WP",  "WEST POINT",

  });
}
