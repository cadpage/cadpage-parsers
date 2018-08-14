package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class NJMonmouthCountyCParser extends DispatchProphoenixParser {
  
  public NJMonmouthCountyCParser() {
    super(CITY_CODES, CITY_LIST, "MONMOUTH COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "ejs1396@gmail.com,ubpolice@unionbeachnj.gov";
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      
"UP", "UNION BEACH"

  });
  
  private static final String[] CITY_LIST = new String[]{

"ALLENHURST",
"ALLENTOWN",
"ALLENWOOD",
"ASBURY PARK",
"ATLANTIC HIGHLANDS",
"AVON-BY-THE-SEA",
"BELFORD",
"BELMAR",
"BRADLEY BEACH",
"BRIELLE",
"CLIFFWOOD BEACH",
"DEAL",
"EAST FREEHOLD",
"EATONTOWN",
"ENGLISHTOWN",
"FAIR HAVEN",
"FAIRVIEW",
"FARMINGDALE",
"FREEHOLD",
"HIGHLANDS",
"INTERLAKEN",
"KEANSBURG",
"KEYPORT",
"LAKE COMO",
"LEONARDO",
"LINCROFT",
"LITTLE SILVER",
"LOCH ARBOUR",
"LONG BRANCH",
"MANASQUAN",
"MATAWAN",
"MONMOUTH BEACH",
"MORGANVILLE",
"NAVESINK",
"NEPTUNE CITY",
"NORTH MIDDLETOWN",
"OAKHURST",
"OCEAN GROVE",
"OCEANPORT",
"PORT MONMOUTH",
"RAMTOWN",
"RED BANK",
"ROBBINSVILLE",
"ROBERTSVILLE",
"ROOSEVELT",
"RUMSON",
"SEA BRIGHT",
"SEA GIRT",
"SHARK RIVER HILLS",
"SHREWSBURY",
"SPRING LAKE",
"SPRING LAKE HEIGHTS",
"STRATHMORE",
"TINTON FALLS",
"UNION BEACH",
"WANAMASSA",
"WEST BELMAR",
"WEST FREEHOLD",
"WEST LONG BRANCH",
"YORKETOWN"

  };
}
