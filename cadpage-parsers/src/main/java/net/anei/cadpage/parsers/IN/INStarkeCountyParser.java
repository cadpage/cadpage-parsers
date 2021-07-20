package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.GroupBestParser;

public class INStarkeCountyParser extends GroupBestParser {

  public INStarkeCountyParser() {
    super(new INStarkeCountyBParser(), new INStarkeCountyCParser());
  }

  public static String baseAdjustMapAddress(String addr) {
    addr = EXWY_PTN.matcher(addr).replaceAll("").trim();
    addr = DIR_OF_PTN.matcher(addr).replaceAll(" & ");
    return addr;
  }
  private static final Pattern EXWY_PTN = Pattern.compile("\\bEXWY\\b");
  private static final Pattern DIR_OF_PTN = Pattern.compile("[/ ]+((?:N|S|E|W|NO|SO|EA|WE|NORTH|SOUTH|EAST|WEST) OF)[/ ]+");

  static final Properties CITY_FIXES = buildCodeTable(new String[]{
      "MONTERY",          "MONTEREY",
      "PUALSKI COUNTY",   "PULASKI COUNTY"
  });

  static final String[] CITY_LIST = new String[]{

    // Cities and towns
    "HAMLET",
    "KNOX",
    "NORTH JUDSON",

    // Unincorporated towns
    "BASS LAKE",
    "ENGLISH LAKE",
    "GROVERTOWN",
    "KOONTZ LAKE",
    "ORA",
    "SAN PIERRE",

    // Townships
    "CALIFORNIA TWP",
    "CENTER TWP",
    "DAVIS TWP",
    "JACKSON TWP",
    "NORTH BEND TWP",
    "OREGON TWP",
    "RAILROAD TWP",
    "WASHINGTON TWP",
    "WAYNE TWP",

    // Jasper County
    "JASPER COUNTY",
    "JASPER CO",
    "WHEATFIELD",

    // Joseph County
    "JOSEPH COUNTY",
    "JOSEPH CO",
    "WALKERTON",

    // LaPorte County
    "LAPORTE COUNTY",
    "LAPORTE CO",
    "LAPORTE",

    // Marshall County
    "MARSHALL COUNTY",
    "HARSHALL CO",
    "CULVER",
    "PLYMOUTH",

    // Porter County
    "PORTER COUNTY",
    "PORTER CO",

    // Pulaski County
    "PULASKI COUNTY",
    "PUALSKI COUNTY", // Misspelled
    "PULASKI CO",
    "DENHAM",
    "MEDARYVILLE",
    "MONTEREY",
    "MONTERY",   // Misspelled
    "PULASKI",
    "WINAMAC"

  };

}
