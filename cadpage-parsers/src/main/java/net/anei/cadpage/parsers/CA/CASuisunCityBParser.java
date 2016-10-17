package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class CASuisunCityBParser extends DispatchA3Parser {
  
  public CASuisunCityBParser() {
    super(0, CITY_CODES, "SUISUN CITY", "CA");
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALNDLE",     "ALLENDALE",
      "AMER CNYN",  "AMERICAN CANYON",
      "BEN",        "BENICIA",
      "CLB",        "CLARKSBURG",
      "CRDLA",      "CORDELIA",
      "CTL",        "COURTLAND",
      "DAVIS",      "DAVIS",
      "DIX",        "DIXON",
      "ELK",        "ELK GROVE",
      "ELMR",       "ELMIRA",
      "FRFLD",      "FAIRFIELD",
      "GRN VLY",    "GREEN VALLEY",
      "ILE",        "ISLETON",
      "ISL",        "ISLETON",
      "MARE IS",    "MARE ISLAND",
      "NAPA",       "NAPA",
      "RIO",        "RIO VISTA",
      "RV",         "RIO VISTA",
      "RYR",        "RYER ISLAND",
      "SAC",        "SACRAMENTO",
      "SNMA",       "SONOMA",
      "ST HELENA",  "SAINT HELENA",
      "SUI",        "SUISUN CITY",
      "TRVS",       "TRAVIS AIR FORCE BASE",
      "VCVL",       "VACAVILLE",
      "VJO",        "VALLEJO",
      "WAL CRK",    "WALNUT CREEK",
      "WAL GRV",    "WALNUT GROVE",
      "WNTRS",      "WINTERS"

  });
}
