package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MOHenryCountyAParser extends DispatchA48Parser {

  public MOHenryCountyAParser() {
    super(CITY_LIST, "HENRY COUNTY", "MO", FieldType.PLACE, A48_ONE_WORD_CODE);
  }

  private static final String[] CITY_LIST = new String[]{

      "BLAIRSTOWN",
      "BROWNINGTON",
      "CALHOUN",
      "CLINTON",
      "CREIGHTON",
      "DEEPWATER",
      "HARTWELL",
      "LA DUE",
      "MONTROSE",
      "TIGHTWAD",
      "URICH",
      "WINDSOR"

  };
}
