package net.anei.cadpage.parsers.MS;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class MSPearlRiverCountyParser extends DispatchA74Parser {

  public MSPearlRiverCountyParser() {
    super(CITY_LIST, "PEARL RIVER COUNTY", "MS");
    setupCities(MAP_CITY_TABLE);
  }

  @Override
  public String getFilter() {
    return "Dispatch@PearlRiverMSe911.info";
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "DERBY",          "POPLARVILLE",
      "HILLSDALE",      "LUMBERTON",
      "LEETOWN",        "LUMBERTON",
      "MILL CREEK",     "CARRIERE",
      "NICHOLSON",      "PICAYUNE",
      "PROGRESS",       "POPLARVILLE",
      "RESTERTOWN",     "POPLARVILLE",
      "SALEM",          "PICAYUNE",
      "SONES CHAPEL",   "POPLARVILLE",
      "SYCAMORE",       "CARRIERE",
  });

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "LUMBERTON",
      "PICAYUNE",
      "POPLARVILLE",

      // Census-designated places
      "HIDE-A-WAY LAKE",
      "NICHOLSON",

      // Other unincorporated communities
      "CAESAR",
      "CARRIERE",
      "CROSSROADS",
      "HENLEYFIELD",
      "MCNEILL",
      "OZONA"



  };
}
