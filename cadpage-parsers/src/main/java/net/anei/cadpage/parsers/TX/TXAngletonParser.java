
package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import net.anei.cadpage.parsers.dispatch.DispatchH02Parser;

public class TXAngletonParser extends DispatchH02Parser {
  public TXAngletonParser() {
    super(CITY_CODES, "ANGLETON", "TX");
  }

  @Override
  public String getFilter() {
    return "nealmorton@bcffa.us";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
        // Cities
        "ALVIN",              "ALVIN",
        "ANGL",               "ANGLETON",
        "BRAZ",               "BRAZORIA",
        "BROOKSIDE VILLAGE",  "BROOKSIDE VILLAGE",
        "CLUTE",              "CLUTE",
        "DANBURY",            "DANBURY",
        "FREEPORT",           "FREEPORT",
        "LAKE JACKSON",       "LAKE JACKSON",
        "LIVERPOOL",          "LIVERPOOL",
        "MANVEL",             "MANVEL",
        "OYSTER CREEK",       "OYSTER CREEK",
        "PEARLAND",           "PEARLAND",
        "RICHWOOD",           "RICHWOOD",
        "SANDY POINT",        "SANDY POINT",
        "SURFSIDE BEACH",     "SURFSIDE BEACH",
        "SWEENY",             "SWEENY",
        "WEST COLUMBIA",      "WEST COLUMBIA",

        // Towns
        "HOLIDAY LAKES",      "HOLIDAY LAKES",
        "QUINTANA",           "QUINTANA",

        // Villages
        "BAILEYS PRAIRIE",    "BAILEYS PRAIRIE",
        "BONNEY",             "BONNEY",
        "HILLCREST",          "HILLCREST",
        "IOWA COLONY",        "IOWA COLONY",
        "JONES CREEK",        "JONES CREEK",

        // CDPs
        "DAMON",              "DAMON",
        "WILD PEACH VILLAGE", "WILD PEACH VILLAGE",

        // Other areas
        "AMSTERDAM",          "AMSTERDAM",
        "ANCHOR",             "ANCHOR",
        "CHENANGO",           "CHENANGO",
        "CHINA GROVE",        "CHINA GROVE",
        "CHOCOLATE BAYOU",    "CHOCOLATE BAYOU",
        "DANCIGER",           "DANCIGER",
        "EAST COLUMBIA",      "EAST COLUMBIA",
        "ENGLISH",            "ENGLISH",
        "OLD OCEAN",          "OLD OCEAN",
        "OTEY",               "OTEY",
        "ROSHARON",           "ROSHARON",
        "SNIPE",              "SNIPE",
        "SILVERLAKE",         "SILVERLAKE",
        "TURTLE COVE",        "TURTLE COVE",
        "MCBETH",             "MCBETH"

  });
}
