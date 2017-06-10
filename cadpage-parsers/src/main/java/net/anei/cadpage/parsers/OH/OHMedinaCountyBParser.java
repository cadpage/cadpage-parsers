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
      "16",       "Dead onArrival",
      "22",       "Drowning",
      "28",       "UnknownFire",
      "2812BC",   "Commercial Fire Alarm",
      "2812BR",   "Residential Fire Alarm",
      "28D",      "Dumpster Fire",
      "28G",      "Grass/Brush Fire",
      "28SC",     "Commercial Structure Fire",
      "28SR",     "Residential Structure Fire",
      "28U",      "Utility/Transformer Fire",
      "28V",      "Vehicle Fire",
      "29",       "Squad/Ambulance",
      "2912B",    "Medical Alarm",
      "4",        "Accident w/Injuries",
      "4PP",      "Private Property Injury Crash",
      "6",        "Airplane Crash",
      "CAVEIN",   "Cave In/Technical Rescue",
      "CO",       "CO Detector no symptoms",
      "CO29",     "CO detector w/symptoms",
      "GASLEAK",  "Gas Leak Natural/Fuel",
      "GASSMELL", "Smell of Gas",
      "GASWELL",  "Gas Well Fire",
      "HAZMAT",   "Spill",
      "SMOKE",    "Smoke Investigation",
      "WEATHER",  "Weather Related Incident",
      "WIRES",    "Wires Down or Sparking"

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
