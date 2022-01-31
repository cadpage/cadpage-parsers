package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MOHenryCountyAParser extends DispatchA48Parser {

  public MOHenryCountyAParser() {
    super(CITY_LIST, "HENRY COUNTY", "MO", FieldType.PLACE, A48_ONE_WORD_CODE);
    setupCallList(CALL_LIST);
  }

  private static final CodeSet CALL_LIST = new  CodeSet(
      "ABANVEH",
      "ALARM",
      "ALARM - ALARM",
      "ANIMAL",
      "ASSIST",
      "ASSIST - ASSIST",
      "BUILDCK",
      "CI",
      "CWB",
      "DEATH",
      "DIST",
      "FIREGRASS - FIRE GRASS",
      "FIRESTRU - FIRE STRUCTURE",
      "FIREVEH - FIRE VEHICLE",
      "FOLLUP",
      "FOUNDPROP",
      "INFORMATION",
      "INVEST",
      "INVEST - INVESTIGATION",
      "MVA",
      "MED",
      "MED - MEDICAL",
      "MISSPER",
      "MVA - MOTOR VEHICLE ACCIDENT",
      "PAPER",
      "PS",
      "RUNAWAY",
      "STEAL",
      "SUSP",
      "TRANSPAT",
      "TRANSPAT - TRANSFER PATIENT",
      "TS",
      "SUICIDE",
      "VISIT",
      "WARRANT"

  );

  private static final String[] CITY_LIST = new String[]{

      "BLAIRSTOWN",
      "BROWNINGTON",
      "CALHOUN",
      "CLINTON",
      "DEEPWATER",
      "HARTWELL",
      "LA DUE",
      "MONTROSE",
      "TIGHTWAD",
      "URICH",
      "WINDSOR"

  };
}
