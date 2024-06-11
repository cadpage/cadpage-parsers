package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class INFountainCountyCParser extends DispatchA48Parser {

  public INFountainCountyCParser() {
    super(INFountainCountyParser.CITY_LIST, "FOUNTAIN COUNTY", "IN", FieldType.GPS_PLACE_APT, A48_OPT_ONE_WORD_CODE);
  }

}
