package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;



public class OHMedinaCountyBParser extends DispatchEmergitechParser {
  
  public OHMedinaCountyBParser() {
    super("Medina911:", CITY_LIST, "MEDINA COUNTY", "OH", TrailAddrType.PLACE);
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    String call = CALL_CODES.getProperty(data.strCall);
    if  (call != null) {
      data.strCode = data.strCall;
      data.strCall = call;
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("CALL", "CODE CALL");
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "12B",      "Alarm Activated",
      "13",       "13 Special Detail",
      "16",       "Dead On Arrival",
      "18",       "Dog Bite",
      "2",        "Accident No Injuries",
      "20",       "Domestic",
      "22",       "Drowning",
      "24",       "Drunk",
      "26SR",     "Fire - Residential Structure",
      "28",       "Fire Unknown",
      "2812BC",   "Fire Alarm - Commercial",
      "2812BR",   "Fire Alarm - Residential",
      "28D",      "Fire - Dumpster/Trash",
      "28G",      "Fire - Grass/Brush",
      "28SC",     "Fire - Commercial Structure",
      "28SR",     "Fire - Residential Structure",
      "28TEST",   "Fire Alarm Testing",
      "28U",      "Fire - Utility Pole/Transformer",
      "28V",      "Fire - Vehicle",
      "29",       "Squad Or Ambulance",
      "2912B",    "Medial Alarm",
      "2PP",      "Private Property Crash",
      "4",        "Accident with Injuries",
      "41",       "Unit Using Portable Radio",
      "42",       "Nature Unknown",
      "4PP",      "Private Property Injury Crash",
      "53",       "Mental",
      "54",       "Stabbing/Cutting",
      "58",       "Suicide",
      "6",        "Airplane Crash",
      "72",       "Lockout",
      "99",       "Emergency Traffic",
      "AMBER",    "Amber Alert",
      "ASSIST",   "ASSIST",
      "BOLO",     "Radio Broadcast",
      "BURN",     "Burning Complaint/Open Burn",
      "CAVEIN",   "Cave In / Tech Rescue",
      "CO",       "CO dectector - No Symptoms",
      "CO29",     "CO Detector W/Symptoms",
      "ELEC",     "Electric Problem",
      "EXPLO",    "Some Type Of Explosion",
      "FIREWORK", "Fireworks",
      "FLOOD",    "Flooded Roadway",
      "GAS WELL", "Fire - Gas Well",
      "GASLEAK",  "Gas Leak (Natural/Fuel)",
      "GASSMELL", "Smell Of Gas In The Area",
      "GASWELL",  "Gas Well Fire",
      "HAZMAT",   "Spill",
      "MISC",     "Miscellaneous",
      "SBA",      "Stand By Assist",
      "SMOKE",    "Smoke Investigation",
      "WATER",    "Water Problem",
      "WEATHER",  "Weather Related Incident",
      "WELFARE",  "Welfare Check",
      "WIRES",    "Wires Down Or Sparking "


  });

  private static final String[] CITY_LIST = new String[]{
      
      "BRUNSWICK",
      "MEDINA",
      "RITTMAN",
      "WADSWORTH",
      
      "CHIPPEWA LAKE",
      "CRESTON",
      "GLORIA GLENS PARK",
      "LODI",
      "SEVILLE",
      "SPENCER",
      "SULLIVAN",
      "WESTFIELD CENTER",
      
      "BRUNSWICK HILLS TWP",
      "CHATHAM TWP",
      "GRANGER TWP",
      "GUILFORD TWP",
      "HARRISVILLE TWP",
      "HINCKLEY TWP",
      "HOMER TWP",
      "LAFAYETTE TWP",
      "LITCHFIELD TWP",
      "LIVERPOOL TWP",
      "MEDINA TWP",
      "MONTVILLE TWP",
      "SHARON TWP",
      "SPENCER TWP",
      "WADSWORTH TWP",
      "WESTFIELD TWP",
      "YORK TWP",
      
      "BEEBETOWN",
      "HOMERVILLE",
      "LITCHFIELD",
      "VALLEY CITY",
      
      // Ashland County
      "MEDINA"
  
  };
}
