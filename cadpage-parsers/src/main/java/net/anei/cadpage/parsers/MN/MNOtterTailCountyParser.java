package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Otter Tail County, MN
 */

public class MNOtterTailCountyParser extends DispatchA27Parser {
  
  public MNOtterTailCountyParser() {
    super(CITY_LIST, "OTTER TAIL COUNTY", "MN", "\\d{8}|[A-Z]+FD|[A-Z]+AMB|[A-Z]{3}\\d{3}[A-Z]{2}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@co.ottertail.mn.us,noreply@cisusa.org";
  }

  private static final String[] CITY_LIST = new String[]{
    "AASTAD",
    "AMOR",
    "AURDAL",
    "BATTLE LAKE",
    "BLOWERS",
    "BLUFFTON",
    "BUSE",
    "BUTLER",
    "CANDOR",
    "CARLISLE",
    "CLITHERALL",
    "COMPTON",
    "CORLISS",
    "DALTON",
    "DANE PRAIRIE",
    "DEAD LAKE",
    "DEER CREEK",
    "DENT",
    "DORA",
    "DUNN",
    "DUNVILLA",
    "EAGLE LAKE",
    "EASTERN",
    "EDNA",
    "EFFINGTON",
    "ELIZABETH",
    "ELMO",
    "ERHARD",
    "ERHARDS GROVE",
    "EVERTS",
    "FERGUS FALLS",
    "FERGUS FALLS",
    "FOLDEN",
    "FRIBERG",
    "GIRARD",
    "GORMAN",
    "HENNING",
    "HOBART",
    "HOMESTEAD",
    "INMAN",
    "LEAF LAKE",
    "LEAF MOUNTAIN",
    "LIDA",
    "MAINE",
    "MAPLEWOOD",
    "NEWTON",
    "NEW YORK MILLS",
    "NIDAROS",
    "NORWEGIAN GROVE",
    "OAK VALLEY",
    "ORWELL",
    "OSCAR",
    "OTTERTAIL",
    "OTTER TAIL",
    "OTTO",
    "PADDOCK",
    "PARKERS PRAIRIE",
    "PARKTON",
    "PELICAN",
    "PELICAN RAPIDS",
    "PERHAM",
    "PINE LAKE",
    "RICHVILLE",
    "ROTHSAY",
    "RUSH LAKE",
    "SCAMBLER",
    "ST. OLAF",
    "STAR LAKE",
    "SVERDRUP",
    "TORDENSKJOLD",
    "TRONDHJEM",
    "TUMULI",
    "UNDERWOOD",
    "URBANK",
    "VERGAS",
    "VINING",
    "WADENA",
    "WESTERN",
    "WOODSIDE"
  };
  
}
