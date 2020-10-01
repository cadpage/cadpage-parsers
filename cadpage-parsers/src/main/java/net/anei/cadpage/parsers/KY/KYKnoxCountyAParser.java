package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class KYKnoxCountyAParser extends DispatchB2Parser {
  
  public KYKnoxCountyAParser() {
    super("911-CENTER:", CITY_LIST, "KNOX COUNTY", "KY", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);

  }
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_ALLOW_DUAL_DIRECTIONS;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "DANIEL BOONE",
      "HENRY MARTIN",
      "LUNDY BRANCH",
      "NOEVILLE HOLLOW",
      "RED BIRD",
      "TYE BEND"
  };

  private CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT W ENTRAP W INJURIES",
      "ACCIDENT WITH INJURIES",
      "BRUSH FIRE",
      "FALL",
      "FIRE ALARM",
      "FIRE MISCELLANEOUS",
      "GAS LEAK",
      "PAIN",
      "SEIZURE",
      "STRUCTURE FIRE",
      "VEHICLE FIRE"
  );
  
  private static final String[] CITY_LIST = new String[]{
      "ARTEMUS",
      "BARBOURVILLE",
      "CORBIN",
      "FLATLICK",
      "GRAY",
      "HEIDRICK",
      "NORTH CORBIN",
      "WOODBINE"
  };
}
