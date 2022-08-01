package net.anei.cadpage.parsers.MS;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class MSPearlRiverCountyParser extends DispatchA86Parser {

  public MSPearlRiverCountyParser() {
    super("PEARL RIVER COUNTY", "MS");
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
}
