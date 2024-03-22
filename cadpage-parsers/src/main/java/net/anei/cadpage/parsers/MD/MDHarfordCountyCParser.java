package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

public class MDHarfordCountyCParser extends DispatchA48Parser {

  public MDHarfordCountyCParser() {
    super(MDHarfordCountyParser.CITY_LIST, "HARFORD COUNTY", "MD", FieldType.PLACE, A48_NO_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "@harfordcountymd.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strPlace.contains("/ -")) {
      data.strCross = data.strPlace.replace("/ -", "/");
      data.strPlace = "";
    }
    return true;
  }

  private static final String[] MWORD_STREET_LIST = new String[] {
      "AYRES CHAPEL",
      "DEEP RUN",
      "EAGLES GROVE",
      "EMORY CHURCH",
      "FAWN GROVE",
      "GRIER NURSERY",
      "SAINT ANNE"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "BRUSH/WOODS/2 BRUSH",
      "BRUSH/WOODS FIRE",
      "BUILDING FIRE",
      "CARDIAC ARREST ALS",
      "CHIMNEY FIRE",
      "FALL BLS",
      "MOTOR VEH COLLISION ALS",
      "MOTOR VEH COLLISION BLS",
      "MOTOR VEH COLL/RESCUE ALS",
      "MVC RESCUE/MULT PT",
      "RESID STRUCT FIRE",
      "SEIZURE BLS",
      "SICK PERSON ALS",
      "SICK PERSON BLS",
      "SMALL STRUCTURE FIRE",
      "STROKE ALS",
      "TEST INCIDENT",
      "TRAUMA BLS",
      "UNCONSCIOUS ALS"
  );

}
