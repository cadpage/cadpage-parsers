package net.anei.cadpage.parsers.MN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

public class MNLeSueurCountyParser extends DispatchA43Parser {
  
  public MNLeSueurCountyParser() {
    super(CITY_LIST, "LE SUEUR COUNTY", "MN");
  }
  
  private static final Pattern NOT_APT_PTN = Pattern.compile("[/0-9]+ MILE", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (NOT_APT_PTN.matcher(apt).lookingAt()) return true;
    return super.isNotExtraApt(apt);
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "CLEVELAND",
      "ELYSIAN",
      "HEIDELBERG",
      "KASOTA",
      "LE CENTER",
      "LE SUEUR",
      "KILKENNY",
      "MANKATO",
      "MONTGOMERY",
      "NEW PRAGUE",
      "WATERVILLE",

      // Townships
      "CLEVELAND",
      "CORDOVA",
      "DERRYNANE",
      "ELYSIAN",
      "KASOTA",
      "KILKENNY",
      "LANESBURGH",
      "LEXINGTON",
      "MONTGOMERY",
      "OTTAWA",
      "SHARON",
      "TYRONE",
      "WASHINGTON",
      "WATERVILLE",

      // Unincorporated communities
      "CORDOVA",
      "GREENLAND",
      "HENDERSON STATION",
      "LEXINGTON",
      "MARYSBURG",
      "OTTAWA",
      "ST HENRY",
      "ST THOMAS",
      "UNION HILL"
  };
}
