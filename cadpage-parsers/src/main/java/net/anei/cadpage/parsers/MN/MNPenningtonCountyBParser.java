package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class MNPenningtonCountyBParser extends DispatchProphoenixParser {

  public MNPenningtonCountyBParser() {
    super(null, CITY_LIST, "PENNINGTON COUNTY", "MN");
  }

  @Override
  public String getFilter() {
    return "noreply@penningtonsheriff.org,@sanfordhealth.org";
  }
  private static final String[] CITY_LIST = new String[] {

          // Cities
          "GOODRIDGE",
          "ST HILAIRE",
          "THIEF RIVER FALLS",

          // Unincorporated communities[edit]
          "DAKOTA JUNCTION",
          "ERIE",
          "HAZEL",
          "HIGHLANDING",
          "KRATKA",
          "MAVIE",
          "RIVER VALLEY",

          // Townships
          "BLACK RIVER TWP",
          "BRAY TWP",
          "CLOVER LEAF TWP",
          "DEER PARK TWP",
          "GOODRIDGE TWP",
          "HICKORY TWP",
          "HIGHLANDING TWP",
          "KRATKA TWP",
          "MAYFIELD TWP",
          "NORDEN TWP",
          "NORTH TWP",
          "NUMEDAL TWP",
          "POLK CENTRE TWP",
          "REINER TWP",
          "RIVER FALLS TWP",
          "ROCKSBURY TWP",
          "SANDERS TWP",
          "SILVERTON TWP",
          "SMILEY TWP",
          "STAR TWP",
          "WYANDOTTE TWP"
  };

}
