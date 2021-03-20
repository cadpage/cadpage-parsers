package net.anei.cadpage.parsers.IA;

import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class IATamaCountyParser extends DispatchA47Parser {

  public IATamaCountyParser() {
    super("Tama County 911", CITY_LIST, "TAMA COUNTY", "IA", "\\d{3,4}|MNFR|MOFR");
  }

  @Override
  public String getFilter() {
    return "swmail@ema.tamacounty.org";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CHELSEA",
      "CLUTIER",
      "DYSART",
      "ELBERON",
      "GARWIN",
      "GLADBROOK",
      "LE GRAND",
      "LINCOLN",
      "MONTOUR",
      "TAMA",
      "TOLEDO",
      "TRAER",
      "VINING",

      // Unincorporated communities
      "BUCKINGHAM",
      "LONG POINT",
      "MESKWAKI SETTLEMENT",

      // Townships
      "BUCKINGHAM",
      "CARLTON",
      "CARROLL",
      "CLARK",
      "COLUMBIA",
      "CRYSTAL",
      "GENESEO",
      "GRANT",
      "HIGHLAND",
      "HOWARD",
      "INDIAN VILLAGE",
      "LINCOLN",
      "ONEIDA",
      "OTTER CREEK",
      "PERRY",
      "RICHLAND",
      "SALT CREEK",
      "SPRING CREEK",
      "TAMA",
      "TOLEDO",
      "YORK"
  };

}
