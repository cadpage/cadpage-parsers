package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDKentCountyAParser extends FieldProgramParser {

  public MDKentCountyAParser() {
    super("KENT COUNTY", "MD",
           "ID CT:ADDR/S0L! BOX:BOX! DUE:UNIT!");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "AUGUSTINE HERMAN",
        "BAY SHORE",
        "BLACKS STATION",
        "BRITTANY BAY",
        "BROAD NECK",
        "CHESTERVILLE BRIDGE",
        "CIRCLE PARK",
        "CLIFFS CITY",
        "COVE POINT",
        "CROMWELL CLARK",
        "DADS DESIRE",
        "DOUBLE CREEK",
        "EASTERN NECK",
        "EDGE OF TOWN",
        "FISH HATCHERY",
        "GRAYS INN CREEK",
        "GREGG NECK",
        "HUMPHREYS POINT",
        "JOHN HANSON",
        "JONES FARM",
        "JUSTIN BUCH",
        "LAMBS MEADOW",
        "LITTLE DUTCHTOWN",
        "MARY MORRIS",
        "MONTABELLO LAKE",
        "PINEY NECK",
        "PRINCESS ANN",
        "QUAKER NECK",
        "RHODE ISLAND",
        "RICAUDS BRANCH",
        "ROCK HALL",
        "ROSEDALE CANNERY",
        "ROUND TOP",
        "SANDY BOTTOM",
        "SPRING COVE",
        "STILL POND NECK",
        "STILL POND",
        "STOCKTON STARTT",
        "TURNERS POINT",
        "WEST HILL",
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
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("#(\\d+)");
    return super.getField(name);
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ALLERGIC REACTION",
      "APPLIANCE FIRE",
      "ASSAULT",
      "BACK PAIN",
      "BREATHING PROBLEMS",
      "BURNS",
      "CARDIAC ARREST",
      "CHEST PAIN",
      "CHIMNEY FIRE",
      "CHOKING",
      "CO ALARM",
      "COASTAL WATER RESCUE",
      "COMM BUILDING FIRE",
      "CONTROLLED BURN",
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
      "GAS LEAK OUTSIDE",
      "GENERAL FIRE ALARM",
      "HEADACHE",
      "HEART PROBLEMS",
      "HEMORRHAGE/LACERATE",
      "INJURED PERSON",
      "INLAND WATER RESCUE",
      "LG STURCTURE FIRE",
      "LG STRUCTURE FIRE",
      "LIFT ASSIST",
      "LOCKED IN/OUT BLD",
      "MATERNITY",
      "MEDICAL ALARM",
      "MEDICAL PRO QA",
      "MUTUAL AID",
      "MVC",
      "MVC/ENTRAPMENT",
      "MVC/PT NOT ALERT",
      "MVC UNK INJURY(S)",
      "MVC W/EJECTION",
      "MVC W/INJURY",
      "MVC W/INJURY(S)",
      "ODOR OF GAS INSIDE",
      "ODOR OF GAS OUTSIDE",
      "ODOR OF SMOKE INSIDE",
      "ODOR OF SMOKE OUTSIDE",
      "ODOR OUTSIDE W/PT'S",
      "OVERDOSE",
      "SEIZURES",
      "SERVICE CALL",
      "SICK PERSON",
      "SMALL OUTSIDE FIRE",
      "SMOKE DETECTOR",
      "SMOKE INVESTIGATION",
      "SM STRCTURE FIRE",
      "SMALL OUTSIDE FIRE",
      "STATION TRANSFER",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE (OUT)",
      "TRANS/PALLIATIVE",
      "UNCONSCIOUS",
      "UNCON/SYNCOPAL",
      "UNK STRUTURE FIRE",
      "UNK TYPE STRUC FIRE",
      "VEH VS PED/BIKE/MOTO",
      "VEHICLE FIRE",
      "WATERFLOW ALARM"
  );
}
