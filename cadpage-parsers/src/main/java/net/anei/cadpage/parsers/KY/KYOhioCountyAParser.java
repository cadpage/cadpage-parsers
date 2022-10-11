package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class KYOhioCountyAParser extends DispatchB2Parser {

  public KYOhioCountyAParser() {
    super("OHIOCO911:", CITY_LIST, "OHIO COUNTY", "KY", B2_FORCE_CALL_CODE);
    setupMultiWordStreets(
        "BELLS RUN",
        "DUNDEE NARROWS",
        "HALLS CREEK",
        "HICKORY LAKE",
        "HORSE BRANCH",
        "MCHENRY CHURCH",
        "ROCK CREEK",
        "TAFFY TOWER",
        "TAYLOR MINE",
        "VINE HILL"
    );
  }

  @Override
  public String getFilter() {
    return "OHIOCO911@interact911.com";
  }

  @Override
  protected CodeSet buildCallList() {
    return new CodeSet(
        "1046 W/DEER",
        "ACCIDENT WITH INJURIES",
        "CHEST PAINS",
        "DIABETIC EMERGENCY",
        "DIFFICULTY BREATHING/SOA",
        "FALL WITH INJURY",
        "FALL WITH NO INJURY/LIFT ASSIS",
        "FIRE BRUSH",
        "FIRE STRUCTURE",
        "MEDICAL OTHER",
        "MEDICAL TRANSPORT",
        "MEDICAL TRANSPORT MEDCO",
        "OVERDOSE/DRUG INCIDENT",
        "SEIZURE",
        "STROKE/CVA",
        "SUICIDE COMMITTED OR ATTEMPT",
        "UNRESPONSIVE"
    );
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BEAVER DAM",
      "CENTERTOWN",
      "FORDSVILLE",
      "HARTFORD",
      "MCHENRY",
      "ROCKPORT",

      // Census-designated places
      "PLEASANT RIDGE",
      "ROSINE",

      // Other unincorporated places
      "ADABURG",
      "BEDA",
      "BUFORD",
      "HAYNESVILLE",
      "HEFLIN",
      "HERBERT",
      "MAGAN",
      "NARROWS",
      "REYNOLDS STATION",
      "SHREVE",
      "SILVER BEACH",
      "TAFFY",
      "BAIZETOWN",
      "CERALVO",
      "COOL SPRINGS",
      "CROMWELL",
      "DUNDEE",
      "ECHOLS",
      "EQUALITY",
      "HORSE BRANCH",
      "MATANZAS",
      "NINETEEN",
      "OLATON",
      "PRENTISS",
      "SELECT",
      "SHULTZTOWN"
  };
}
