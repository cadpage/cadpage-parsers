package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.GroupBestParser;

public class ILIroquoisCountyParser extends GroupBestParser {

  public ILIroquoisCountyParser() {
    super(new ILIroquoisCountyAParser(), new ILIroquoisCountyBParser());
  }

  static final String[] CITY_LIST = new String[]{

    // Cities
    "GILMAN",
    "WATSEKA",

    // Villages
    "ASHKUM",
    "BEAVERVILLE",
    "BUCKLEY",
    "CHEBANSE",
    "CISSNA PARK",
    "CLIFTON",
    "CRESCENT CITY",
    "DANFORTH",
    "DONOVAN",
    "IROQUOIS",
    "LODA",
    "MARTINTON",
    "MILFORD",
    "ONARGA",
    "PAPINEAU",
    "SHELDON",
    "STOCKLAND",
    "THAWVILLE",
    "WELLINGTON",
    "WOODLAND",

    // Townships
    "ASH GROVE TWP",
    "BEAVER TWP",
    "BELMONT TWP",
    "CHEBANSE TWP",
    "CONCORD TWP",
    "LODA TWP",
    "MIDDLEPORT TWP",
    "MILFORD TWP",
    "ONARGA TWP",
    "PAPINEAU TWP",
    "STOCKLAND TWP",
    "MARTINTON TWP",
    "IROQUOIS TWP",
    "PRAIRIE GREEN TWP",
    "ASHKUM TWP",
    "DOUGLAS TWP",
    "ARTESIA TWP",
    "FOUNTAIN CREEK TWP",
    "LOVEJOY TWP",
    "SHELDON TWP",
    "MILKS GROVE TWP",
    "PIGEON GROVE TWP",
    "CRESCENT TWP",
    "DANFORTH TWP",
    "RIDGELAND TWP",
    "BEAVERVILLE TWP"


  };
}
