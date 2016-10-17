package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.GroupBestParser;

public class TNWilliamsonCountyParser extends GroupBestParser {
  
  public TNWilliamsonCountyParser() {
    super(new TNWilliamsonCountyAParser(), new TNWilliamsonCountyBParser());
  }

  static final String[] CITY_LIST = new String[]{
    
    // Cities & Towns
    "BRENTWOOD",
    "FAIRVIEW",
    "FRANKLIN",
    "NOLENSVILLE",
    "SPRING HILL",
    "THOMPSONS STATION",
    
    // Unincorporated Communities
    "ALLISONA",
    "ARRINGTON",
    "BERRY'S CHAPEL",
    "BETHESDA",
    "BETHLEHEM",
    "BOSTON",
    "BURWOOD",
    "COLLEGE GROVE",
    "CLOVERCROFT",
    "COOL SPRINGS",
    "FERNVALE",
    "GRASSLAND",
    "KIRKLAND",
    "LEIPER'S FORK",
    "LIBERTY HILL",
    "PEYTONSVILLE",
    "PRIMM SPRINGS",
    "RUDDERVILLE",
    "SOUTHALL",
    "TRIUNE",
    
    // Davidson County
    "ANTIOCH",
    "NASHVILLE",
    
    // Hickman County
    "HICKMAN CO"
  };

}
