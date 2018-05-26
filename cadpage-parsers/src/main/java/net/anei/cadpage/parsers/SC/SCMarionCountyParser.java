package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;


public class SCMarionCountyParser extends DispatchA48Parser {

  public SCMarionCountyParser() {
    super(CITY_LIST, "MARION COUNTY", "SC", FieldType.MAP, A48_NO_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  private String savePlace = null;
  
  private static final Pattern MARKER = Pattern.compile("([A-Z0-9]+) ON DISPATCH(?=:)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    String source = match.group(1);
    body = body.substring(match.end());
    
    body = stripFieldEnd(body, "SC - - LL");
    body = stripFieldEnd(body, "SC - -");
    savePlace = null;
    if (!super.parseMsg(subject, body, data)) return false;
    data.strSource = source;
    if (savePlace != null) data.strPlace = append(savePlace, " - ", data.strPlace);
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram() + " PLACE";
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
      "BOBBY L DAVIS",
      "BYPASS SAWYER",
      "GOLDEN MACK",
      "HENRY MCGILL",
      "MAIDEN DOWN",
      "SAND HILL",
      "WAVERLY WAY",
      "WEST MULLINS"
  };
  

  private static final CodeSet CALL_LIST = new CodeSet(
      "ASTDSS",
      "BRUSH FIRE",
      "BURGLAR ALARM (RESIDENTIAL)",
      "CARDIAC ARREST",
      "CHOKING",
      "CHEST PAIN",
      "CHESTP",
      "CIVILD",
      "DIABETIC",
      "DISORDERLY CONDUCT",
      "ELECTRICAL FIRE",
      "FALL WITH INJURY",
      "FALVIC",
      "FIRE ALARM COMMERCIAL",
      "FIRE ALARM RESIDENTIAL",
      "GAS LEAK",
      "HEART PROBLEMS",
      "INFORMATION",
      "INVESTIGATION",
      "LIFTAS",
      "LIFTING ASSISTANCE",
      "MEDICAL ALARM",
      "MOTOR VEHICLE ACCIDENT",
      "MVA CONFIRMED ENTRAPMENT",
      "MVA MUTIPLE INJURIES",
      "OB-GYN EMERGENCY",
      "PAIN",
      "REPORT",
      "RESPIRATORY DISTRESS",
      "ROAD HAZARD",
      "SEIZURE",
      "STABBING MUTIPLE VICTIMS",
      "STANBY",
      "STROKE",
      "STROKE /CVA",
      "STRUCTURE FIRE (COMMERCIAL)",
      "STRUCTURE FIRE",
      "SUSPICOUS PERSON",
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
