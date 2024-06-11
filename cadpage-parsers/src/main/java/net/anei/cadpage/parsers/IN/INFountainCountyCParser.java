package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class INFountainCountyCParser extends DispatchA48Parser {

  public INFountainCountyCParser() {
    this("FOUNTAIN COUNTY", "IN");
  }

  public INFountainCountyCParser(String defCity, String defState) {
    super(null, defCity, defState, FieldType.GPS_PLACE_APT, A48_OPT_ONE_WORD_CODE);
  }

  @Override
  public String getAliasCode() {
    return "INFountainCountyC";
  }

}
