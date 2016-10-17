package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA36Parser;


public class GABrantleyCountyParser extends DispatchA36Parser {
 
  public GABrantleyCountyParser() {
    super(CITY_LIST, "BRANTLEY COUNTY", "GA", 1);
    setupMultiWordStreets(
        "EAGLE NEST",
        "MATTIE SHUMAN",
        "SADDLE CLUB",
        "WARE COUNTY LINE",
        "WHITEHALL CHURCH"
    );
  }
  
  @Override
  public String getFilter() {
    return "brantleyga@ez911mail.com";
  }
  
  private static String[] CITY_LIST = new String[]{
    "ATKINSON",
    "HICKOX",
    "HOBOKEN",
    "HORTENSE",
    "NAHUNTA",
    "SCHLATTERVILLE",
    "TRUDIE",
    "WAYNESVILLE"
  };
 }
