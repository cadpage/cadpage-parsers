package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * McLean County, KY
 */
public class KYMcLeanCountyParser extends DispatchEmergitechParser {
  
  public KYMcLeanCountyParser() {
    super("Dispatch:", CITY_LIST, "MCLEAN COUNTY", "KY", TrailAddrType.INFO);
    setupCallList(CALL_LIST);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.contains("COMMENTS:")) {
      int pt = body.indexOf(" // ");
      if (pt >= 0) {
        body = body.substring(0,pt) + " COMMENTS: " + body.substring(pt+4).trim();
      }
    }
    // TODO Auto-generated method stub
    return super.parseMsg(body, data);
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ANNOUNCEMENT",
      "ARREST BLOTTER",
      "ASSAULT",
      "AUTO ACCIDENT - NO INJURY",
      "AUTO ACCIDENT NO INJURIES",
      "AUTO ACCIDENT WITH INJURIES",
      "BLEEDING",
      "BLOOD PRESSURE HIGH/LOW",
      "BREATHING PROBLEMS",
      "BURNING - CONTROLLED",
      "CHEST PAIN",
      "COMPLAINT",
      "DEER/VEH/DEAD",
      "DIABETIC PROBLEM",
      "EXTRA PATROL",
      "FALL",
      "FALLS - GROUND LEVEL - OTHER",
      "FIRE - REKINDLE",
      "FIRE - STRUCTURE",
      "FIRE - TREES, BRUSH, GRASS",
      "GENERAL MEDICAL",
      "HEADACHES",
      "HEART ATTACK",
      "HEART / FAST /SLOW",
      "MUTUAL AID",
      "OVERDOSE",
      "PAIN",
      "SEIZURES",
      "STROKE",
      "SUICIDE",
      "SUICIDE-ATTEMPT",
      "SUICIDE-THREATENING",
      "SUSPICIOUS PERSON",
      "SUSPICIOUS VEHICLE",
      "ROAD CLOSED",
      "STAND BY/RESPOND",
      "TEST",
      "TRANSFER PATIENT",
      "UNKNOWN MEDICAL EMERGENCY",
      "UNRESPONSIVE",
      "VEH/ANIMAL",
      "VEHICLE INSPECTION",
      "WALK-IN",
      "WEATHER",
      "WEATHER ALERT",
      "WELL BEING CHECK"
  );
  
  private static final String[] CITY_LIST = new String[]{

      "BEECH GROVE",
      "BUEL",
      "BUTTONSBERRY",
      "CALHOUN",
      "CLEOPATRA",
      "COMER",
      "CONGLETON",
      "ELBA",
      "GLENVILLE",
      "GUFFIE",
      "ISLAND",
      "LEMON",
      "LIVERMORE",
      "LIVIA",
      "NUCKOLS",
      "OWENSBORO",
      "PACK",
      "POPLAR GROVE",
      "POVERTY",
      "RUMSEY",
      "SACRAMENTO",
      "SEMIWAY",
      "UTICA",
      "WRIGHTSBURG",
      "WYMAN"
  };
}
