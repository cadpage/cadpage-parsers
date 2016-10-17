package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchArchonixParser;



public class NJCamdenCountyAParser extends DispatchArchonixParser {
  
  public NJCamdenCountyAParser() {
    super(CITY_CODES, "CAMDEN COUNTY", "NJ");
    setupSaintNames("JOHN", "MARK", "MORITZ");
  }
  
  @Override
  public String getFilter() {
    return "cccademail@camdencounty.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
    "01",    "Audubon",
    "02",    "Audubon Park",
    "03",    "Barrington",
    "04",    "Bellmawr",
    "05",    "Berlin",
    "06",    "Berlin Twp",
    "07",    "Brooklawn",
    "08",    "Camden City",
    "09",    "Chesilhurst",
    "10",    "Clementon",
    "11",    "Collingswood",
    "12",    "Cherry Hill",
    "13",    "Gibbsboro",
    "14",    "Gloucester City",
    "15",    "Gloucester Twp",
    "16",    "Haddon Twp",
    "17",    "Haddonfield",
    "18",    "Haddon Heights",
    "19",    "Hi-Nella",
    "20",    "Laurel Springs",
    "21",    "Lawnside",
    "22",    "Lindenwold",
    "23",    "Magnolia",
    "24",    "Merchantville",
    "25",    "Mount Ephraim",
    "26",    "Oaklyn",
    "27",    "Pennsauken Twp",
    "28",    "Pine Hill",
    "29",    "Pine Valley",
    "30",    "Runnemede",
    "31",    "Somerdale",
    "32",    "Stratford",
    "33",    "Tavistock",
    "34",    "Voorhees Twp",
    "35",    "Waterford Twp",
    "36",    "Winslow Twp",
    "37",    "Woodlyne",
    "44",    "",
    "63",    "",
  });

}
