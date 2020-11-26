package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

public class MNMowerCountyAParser extends DispatchA43Parser {

  public MNMowerCountyAParser() {
    super(CITY_LIST, "MOWER COUNTY", "MN");
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "ADAMS",
      "AUSTIN",
      "BROWNSDALE",
      "DEXTER",
      "ELKTON",
      "GRAND MEADOW",
      "LE ROY",
      "LYLE",
      "MAPLEVIEW",
      "RACINE",
      "ROSE CREEK",
      "SARGEANT",
      "TAOPI",
      "WALTHAM",

      // Townships
      "ADAMS",
      "AUSTIN",
      "BENNINGTON",
      "CLAYTON",
      "DEXTER",
      "FRANKFORD",
      "GRAND MEADOW",
      "LANSING",
      "LE ROY",
      "LODI",
      "LYLE",
      "MARSHALL",
      "NEVADA",
      "PLEASANT VALLEY",
      "RACINE",
      "RED ROCK",
      "SARGEANT",
      "UDOLPHO",
      "WALTHAM",
      "WINDOM",

      // Census-designated place
      "LANSING",

      // Other unincorporated communities
      "ANDYVILLE",
      "CORNING",
      "JOHNSBURG",
      "MAYVILLE",
      "NICOLVILLE",
      "RAMSEY",
      "RENOVA",
      "VARCO",
      
      // Scott County
      "PRIOR LAKE",
      "SAVAGE",
      "SHAKOPEE"
  };
}
