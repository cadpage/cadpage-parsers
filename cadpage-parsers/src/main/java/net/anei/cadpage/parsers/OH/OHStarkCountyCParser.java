package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA39Parser;

public class OHStarkCountyCParser extends DispatchA39Parser {

  public OHStarkCountyCParser() {
    super(CITY_LIST, "STARK COUNTY", "OH");
  }
  
  @Override
  public String getFilter() { 
    return "notifications@civicready.com"; 
  }

  private static final String[] CITY_LIST = new String[]{

   //Cities

      "ALLIANCE",
      "CANAL FULTON",
      "CANTON",
      "LOUISVILLE",
      "MASSILLON",
      "NORTH CANTON",

  //Villages

      "BEACH CITY",
      "BREWSTER",
      "EAST CANTON",
      "EAST SPARTA",
      "HARTVILLE",
      "HILLS AND DALES",
      "LIMAVILLE",
      "MAGNOLIA",
      "MINERVA",
      "MEYERS LAKE",
      "NAVARRE",
      "WAYNESBURG",
      "WILMOT",

  //Townships

      "BETHLEHEM",
      "CANTON",
      "JACKSON",
      "LAKE",
      "LAWRENCE",
      "LEXINGTON",
      "MARLBORO",
      "NIMISHILLEN",
      "OSNABURG",
      "PARIS",
      "PERRY",
      "PIKE",
      "PLAIN",
      "SANDY",
      "SUGAR CREEK",
      "TUSCARAWAS",
      "WASHINGTON",

  //Census-designated places

      "GREENTOWN",
      "MARLBORO",
      "NORTH LAWRENCE",
      "PERRY HEIGHTS",
      "RICHVILLE",
      "ROBERTSVILLE",
      "UNIONTOWN",

  //Other unincorporated communities

      "AVONDALE",
      "CAIRO",
      "CRYSTAL SPRINGS",
      "EAST GREENVILLE",
      "FREEBURG",
      "JUSTUS",
      "MAPLETON",
      "MARCHAND",
      "MAXIMO",
      "MCDONALDSVILLE",
      "MIDDLEBRANCH",
      "NEW BALTIMORE",
      "NEW FRANKLIN",
      "NEWMAN",
      "NORTH INDUSTRY",
      "PARIS",
      "PIGEON RUN",
      "SIPPO",
      "WACO"
  };
}
