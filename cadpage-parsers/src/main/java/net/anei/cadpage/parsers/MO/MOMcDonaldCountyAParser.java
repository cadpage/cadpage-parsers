package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MOMcDonaldCountyAParser extends DispatchA48Parser {
  
  public MOMcDonaldCountyAParser() {
    super(CITY_LIST, "MCDONALD COUNTY", "MO", FieldType.TRASH);
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "lisa@mc-911.org";
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "LANDING ZONE F",
      "LARGE F",
      "RADIO/PAGE TEST FIRE/EMS ALL"
  );

  private static final String[] CITY_LIST = new String[]{

    "MCDONALD",

    "ANDERSON",
    "CAVERNA",
    "GINGER BLUE",
    "GOODMAN",
    "HART",
    "JACKET",
    "JANE",
    "LANAGAN",
    "LONGVIEW",
    "NOEL",
    "PINEVILLE",
    "POWELL",
    "ROCKY COMFORT",
    "SOUTHWEST CITY",
    "TIFF CITY"
  };

}
