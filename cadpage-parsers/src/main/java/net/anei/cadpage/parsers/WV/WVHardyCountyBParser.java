package net.anei.cadpage.parsers.WV;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Hardy County, WV (B)
 */
public class WVHardyCountyBParser extends DispatchA48Parser {

  public WVHardyCountyBParser() {
    super(CITY_LIST, "HARDY COUNTY", "WV", FieldType.X_NAME,
          Pattern.compile("[A-Z]\\d+[A-Z]?|\\d{4}"));
    setupCallList(CALL_CODE);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "Hardy911@hardynet.com,HARDYCOE911@hardynet.com,OnDispatch@hardynet.com";
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
      "APPALACHIAN CORR",
      "APPLE TREE",
      "ASHTON WOODS",
      "COLD SPRINGS",
      "CRAB APPLE",
      "CRITES FARM",
      "CUNNINGHAM WOODS",
      "DEAD END",
      "DOVER HOLLOW",
      "DUMPLING RUN",
      "FOREST GLEN",
      "FORK ESTATES",
      "GENNARO'S WINCHESTER",
      "HIGHLAND VIEW",
      "HINKLE MOUNTAIN",
      "HOWARDS LICK",
      "JENKINS RUN",
      "MILL ISLAND",
      "MLES S BRANCH RIVER",
      "MOUNT OLIVE",
      "OPPOSUM HOLLOW",
      "OURS FARM",
      "POOR PEAR",
      "REYNOLDS GAP",
      "RIO SHADE VALLEY",
      "ROBERT C BYRD INDUSTRIAL PARK",
      "RON MACE",
      "SILVER OAK",
      "WATER PLANT",
      "WATER STREET",
      "WHITE TAIL"
  };

  private static final CodeSet CALL_CODE = new CodeSet(
      "ABDOMINAL PAIN",
      "ALLERGIC REACTION",
      "BREATHING PROBLEMS",
      "BRUSH FIRE",
      "CARBON MONOXIDE ALARM",
      "CARBON MONOXIDE- NO SYMPTOMS",
      "CARDIAC ARREST",
      "CARDIAC EMERGENCY",
      "CHEST PAIN",
      "CHOKING",
      "DEAD ON ARRIVAL",
      "DOMESTIC REPORT",
      "DWELLING FIRE",
      "FALL",
      "FIRE ALARM",
      "FIRE GENERIC",
      "FLU FIRE",
      "GAS LEAK",
      "GENERAL ILLNESS",
      "MEDICAL ALARM",
      "MISSING PERSON REPORT",
      "MOTOR VEHICLE ACCIDENT",
      "MUTUAL AID",
      "ORDER INVESTIGATION/COMPLAINT",
      "RUBBISH/TRASH FIRE",
      "RUNAWAY/MISSING JUVENILE",
      "SEIZURE",
      "SMOKE INVESTIGATION",
      "STRUCTURE FIRE",
      "SUICIDE",
      "TRANSPORT PRISONER",
      "TRAUMATIC INJURIES",
      "TREE DOWN",
      "UNCONSIOUS PERSON",
      "VEHICLE FIRE",
      "WELFARE CHECK",
      "WIRES DOWN (NO FIRE)"

  );

  private static final String[] CITY_LIST = new String[]{

    "ARKANSAS",
    "BAKER",
    "BASORE",
    "BASS",
    "BAUGHMAN",
    "BEAN",
    "BRAKE",
    "CUNNINGHAM",
    "DURGON",
    "FISHER",
    "FLATS",
    "FORT RUN",
    "INKERMAN",
    "KESSEL",
    "LOST CITY",
    "LOST RIVER",
    "MATHIAS",
    "MCCAULEY",
    "MCNEILL",
    "MILAM",
    "MOOREFIELD",
    "NEEDMORE",
    "OLD FIELDS",
    "PERRY",
    "PERU",
    "RIG",
    "ROCK OAK",
    "ROCKLAND",
    "TANNERY",
    "TAYLOR",
    "WALNUT BOTTOM",
    "WARDENSVILLE"

  };
}
