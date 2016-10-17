package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGlobalDispatchParser;



public class MOGasconadeCountyParser extends DispatchGlobalDispatchParser {
  
  private static final Pattern UNIT_PTN = Pattern.compile("(?:[GOMB]|MT)?\\d{2,3}");
  
  public MOGasconadeCountyParser() {
    super(CITY_TABLE, "GASCONADE COUNTY", "MO", LEAD_SRC_UNIT_ADDR | PLACE_FOLLOWS_CALL, null, UNIT_PTN);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "MARIES COUNTY",
        "NURSING HOME",
        "LAKE NORTHWOODS",
        "PIGG HOLLOW",
        "PUMP STATION",
        "ROLLING HOME"
    );
  }
  
  @Override
  public String getFilter() {
    return "central@fidmail.com,gc911text@gmail.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("MESSAGE / ")) body = body.substring(10).trim();
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("GASCONADE COUNTY")) data.strCity = "";
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ALARM SOUNDING- EMS",
      "ALLERGIC REACTION",
      "ACCIDENTAL INJURY",
      "ASSIST AN INVALID",
      "BACK PAIN",
      "CARDIAC ARREST",
      "CHEST PAIN",
      "DIABETIC PROBLEMS",
      "DIFFICULTY BREATHING",
      "FALL",
      "FALL- PERSON FELL",
      "GAS LEAK OUTSIDE",
      "HEADACHE",
      "HEART PROBLEMS",
      "MOTOR VEHICLE ACCIDENT-INJURIES",
      "MOTOR VEHICLE ACCIDENT-UNKNOWN INJ",
      "MOTOR VEHICLE ACCIDENT WITH RESCUE",
      "MOVE UP",
      "OBS/ NON VIOLENT",
      "POISONING",
      "SEIZURES",
      "SICK CASE",
      "SICK PERSON- SICK CASE",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE- RESIDENTIAL",
      "SUICIDAL PERSON-SCENE SECURE",
      "TRANSFER- ROUTINE",
      "TRAUMATIC INJURY",
      "UNRESPONSIVE PERSON",
      "WALK IN TO AMB BUILDING"
  );

  private static final String[] CITY_TABLE = new String[]{
    "BLAND",
    "BELLE",
    "GASCONADE",
    "HERMANN",
    "MORRISON",
    "MT STERLING",
    "OWENSVILLE",
    "ROSEBUD",
    "GASCONADE COUNTY",
    "OUT OF COUNTY",
    "OSAGE COUNTY"
  };
}
