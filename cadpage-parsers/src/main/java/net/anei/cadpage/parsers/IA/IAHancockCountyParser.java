package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class IAHancockCountyParser extends DispatchA47Parser {
  
  public IAHancockCountyParser() {
    super("from HCSO Dispatch", CITY_LIST, "HANCOCK COUNTY", "IA", ".*");
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "BRITT",
    "CORWITH",
    "CRYSTAL LAKE",
    "FOREST CITY",
    "GARNER",
    "GOODELL",
    "KANAWHA",
    "KLEMME",
    "WODEN",

    // Unincorporated communities
    "DUNCAN",
    "HAYFIELD",
    "HUTCHINS",
    "MILLER",
    "STILSON",

    // Townships
    "AMSTERDAM",
    "AVERY",
    "BINGHAM",
    "BOONE",
    "BRITT",
    "CONCORD",
    "CRYSTAL",
    "ELL",
    "ELLINGTON",
    "ERIN",
    "GARFIELD",
    "LIBERTY",
    "MADISON",
    "MAGOR",
    "ORTHEL",
    "TWIN LAKE",
    
    // Cerro Gordo County
    "VENTURA",
    
    "KOSSUTH COUNTY"
  };
}
