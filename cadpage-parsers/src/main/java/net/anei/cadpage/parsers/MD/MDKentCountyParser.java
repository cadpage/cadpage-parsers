package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDKentCountyParser extends FieldProgramParser {

  public MDKentCountyParser() {
    super("KENT COUNTY", "MD",
           "CT:ADDR/S0L! BOX:BOX! DUE:UNIT!");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "AUGUSTINE HERMAN",
        "BAY SHORE",
        "BRITTANY BAY",
        "BROAD NECK",
        "CLIFFS CITY",
        "CROMWELL CLARK",
        "DOUBLE CREEK",
        "JONES FARM",
        "LAMBS MEADOW",
        "PRINCESS ANN",
        "QUAKER NECK",
        "RICAUDS BRANCH",
        "ROUND TOP",
        "STILL POND",
        "WYMONT PARK"
    );
  }
  
  @Override
  public String getFilter() {
    return "911@kentgov.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strBox.equals("QA")) {
      data.strCity = "QUEEN ANNES COUNTY";
    }
    else if (data.strBox.equals("OOC")) {
      data.defCity = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " CITY";
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ALLERGIC REACTION",
      "APPLIANCE FIRE",
      "ASSAULT",
      "BREATHING PROBLEMS",
      "CARDIAC ARREST",
      "CHEST PAIN",
      "CHIMNEY FIRE",
      "CHOKING",
      "CO ALARM",
      "COMM BUILDING FIRE",
      "CVA",
      "DIABETIC PROBLEMS",
      "DWELLING FIRE",
      "ELEVATOR RESCUE",
      "EMOTIONAL DISORDER",
      "EXPLOSION",
      "FAINTING",
      "FALL",
      "FALL (LIFT ASSIST)",
      "FALL NOT ALERT",
      "FUEL SPILL",
      "GAS LEAK INSIDE",
      "GENERAL FIRE ALARM",
      "HEMORRHAGE/LACERATE",
      "INJURED PERSON",
      "MATERNITY",
      "MEDICAL ALARM",
      "MUTUAL AID",
      "MVC",
      "MVC/ENTRAPMENT",
      "MVC/PT NOT ALERT",
      "MVC UNK INJURY(S)",
      "MVC W/EJECTION",
      "MVC W/INJURY(S)",
      "ODOR OF GAS INSIDE",
      "ODOR OF GAS OUTSIDE",
      "ODOR OF SMOKE INSIDE",
      "ODOR OF SMOKE OUTSIDE",
      "SEIZURES",
      "SERVICE CALL",
      "SICK PERSON",
      "SMOKE DETECTOR",
      "SM STRCTURE FIRE",
      "STATION TRANSFER",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE (OUT)",
      "TRANS/PALLIATIVE",
      "UNCONSCIOUS",
      "UNCON/SYNCOPAL",
      "UNK STRUTURE FIRE",
      "VEHICLE FIRE",
      "WATERFLOW ALARM"
  );
}
