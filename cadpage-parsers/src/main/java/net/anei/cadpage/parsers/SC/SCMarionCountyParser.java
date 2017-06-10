package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;


public class SCMarionCountyParser extends DispatchA48Parser {

  public SCMarionCountyParser() {
    super(CITY_LIST, "MARION COUNTY", "SC", FieldType.NONE, A48_NO_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  private String savePlace = null;
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldEnd(body, "SC - - LL");
    savePlace = null;
    if (!super.parseMsg(subject, body, data)) return false;
    if (savePlace != null) data.strPlace = append(savePlace, " - ", data.strPlace);
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " PLACE";
  }
  
  @Override
  public String fixCallAddress(String field) {
    int pt = field.indexOf("//");
    if (pt >= 0) {
      savePlace = field.substring(pt+2).trim();
      field = field.substring(0, pt);
    }
    return field;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BYPASS SAWYER",
      "HENRY MCGILL",
      "WEST MULLINS"
  };
  

  private static final CodeSet CALL_LIST = new CodeSet(
      "ASTDSS",
      "BRUSH FIRE",
      "BURGLAR ALARM (RESIDENTIAL)",
      "CARDIAC ARREST",
      "CHOKING",
      "CHESTP",
      "CIVILD",
      "DIABETIC",
      "FALL WITH INJURY",
      "FALVIC",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM RESIDENTIAL",
      "GAS LEAK",
      "INFORMATION",
      "INVESTIGATION",
      "LIFTAS",
      "LIFTING ASSISTANCE",
      "MEDICAL ALARM",
      "MOTOR VEHICLE ACCIDENT",
      "PAIN",
      "REPORT",
      "RESPIRATORY DISTRESS",
      "SEIZURE",
      "STANBY",
      "STROKE",
      "STROKE /CVA",
      "STRUCTURE FIRE (COMMERCIAL)",
      "STRUCTURE FIRE",
      "UNCONSCIOUS-UNREPONSIVE"
  );

  private static final String[] CITY_LIST = new String[]{
      "ARIEL CROSSROAD",
      "BRITTONS NECK",
      "CENTENARY",
      "FRIENDSHIP",
      "GRESHAM",
      "MARION",
      "MULLINS",
      "NICHOLS",
      "RAINS",
      "SELLERS",
      "TEMPERANCE HILL"
  };
}
