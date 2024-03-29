package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;


public class IAWorthCountyParser extends DispatchA47Parser {

  public IAWorthCountyParser() {
    super("Dispatch info", CITY_LIST, "WORTH COUNTY", "IA", "\\d{1,3}|[A-Z0-9]{1,3}FD|[FML][CM]A|ISP|LMA|MCA\\d?");
  }

  @Override
  public String getFilter() {
    return "swmail@worthcounty.org";
  }

  private static final String[] CITY_LIST =new String[]{

    //cities

    "FERTILE",
    "GRAFTON",
    "HANLONTOWN",
    "JOICE",
    "KENSETT",
    "MANLY",
    "NORTHWOOD",

    //Unincorporated community

    "BOLAN",

    //Townships

    "BARTON",
    "BRISTOL",
    "BROOKFIELD",
    "DANVILLE",
    "DEER CREEK",
    "FERTILE",
    "GROVE",
    "HARTLAND",
    "KENSETT",
    "LINCOLN",
    "SILVER LAKE",
    "UNION",

    // Cero Gorda County
    "PLYMOUTH",

    // Mitchell County
    "SAINT ANSGAR",

    // Winnebago County
    "LAKE MILLS"
  };
}
