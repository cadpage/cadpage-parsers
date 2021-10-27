package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class LAWestFelicianaParishParser extends DispatchA48Parser {

  public LAWestFelicianaParishParser() {
    super(CITY_LIST, "WEST FELICIANA PARISH", "LA", FieldType.GPS_PLACE_X, A48_NO_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSaintNames("FRANCISVILLE");
  }


  @Override
  public String getFilter() {
    return "911CENTER@wfpso.org";
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
      "BAINS RISTROPH",
      "CAP EDDIES",
      "CLUBHOUSE WAY",
      "DANIEL PORCHE",
      "DEER RUN",
      "FOREST GLEN",
      "INDIAN MOUND",
      "JONES VAUGHN CREEK",
      "JONES VAUGN CREEK",
      "KIRKS CROSSING",
      "L B HILL",
      "LA PETITE",
      "LAKE HILLS",
      "LAUREL HILL",
      "LIVE OAK",
      "LOW WATER BRIDGE",
      "MCNEAL PARK",
      "MELINDA LEE",
      "MULBERRY HILL",
      "MYRTLE HILL",
      "OAK GROVE",
      "RIVER BEND ACCESS",
      "ROBERT BAILEY",
      "SAGE HILL",
      "SHADY GROVE",
      "ST MARYS",
      "STAR HILL",
      "THOMPSON COVE",
      "TRAILER PARK",
      "WEST FELICIANA"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "911",
      "FALARM",
      "GFIRE",
      "MALARM",
      "MED",
      "MENTAL",
      "MVAU",
      "SMOKE",
      "UNRESP",
      "VFIRE"
  );

  private static final String[] CITY_LIST = new String[] {
      "ANGOLA",
      "BAINS",
      "ST FRANCISVILLE",
      "TUNICA",
      "WAKEFIELD"
  };

}
