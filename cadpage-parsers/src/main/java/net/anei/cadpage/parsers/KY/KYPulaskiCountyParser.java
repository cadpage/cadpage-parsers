package net.anei.cadpage.parsers.KY;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class KYPulaskiCountyParser extends DispatchA65Parser {

  public KYPulaskiCountyParser() {
    super(CITY_LIST, "PULASKI COUNTY", "KY");
    setupCities(CITY_CODES);
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info,dispatch@911comm3.info,cadreports@pulaski911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;

    data.strCode = data.strCall;
    data.strCall = convertCodes(data.strCode, CALL_CODES);

    data.strCity = convertCodes(data.strCity.toUpperCase(), CITY_CODES);
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replaceAll("CALL", "CODE CALL");
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "1",      "Receiving Poorly",
      "2",      "Receiving Well",
      "3",      "Stop Transmitting",
      "4",      "ok-Affirmative",
      "5",      "Relay",
      "6",      "Busy",
      "7",      "Out of Service",
      "8",      "In Service",
      "9",      "Repeat",
      "10",     "Out of Car,Subject to Call",
      "12",     "Officials or Visitors Present",
      "13",     "Weather Conditions/Road Conditions",
      "14",     "Escort",
      "15",     "Prisoner in Custody",
      "19",     "Request for Coroner",
      "20",     "Location",
      "23",     "Everything OK",
      "24",     "Trouble at Dispatch-please respond",
      "25",     "Contact with...",
      "27",     "Arrived with patient at...",
      "29",     "Check for wanted or stolen",
      "30",     "Does not conform with rules and regulations",
      "33",     "Emergency Traffic at your Station",
      "36",     "Correct Time",
      "37",     "Dispatcher on Duty",
      "45",     "Property Damage Accident-Motor Vehicle",
      "46",     "Personal Injury Accident-Motor Vehicle",
      "47",     "Vehicle Registration Check",
      "51",     "Enroute to...",
      "53",     "Motorist Assist",
      "55",     "Active Alarm-Robbery or Burglary in Progress",
      "60",     "Change to Tactical Frequency",
      "61",     "Change to Scrambled Frequency",
      "67",     "Gas Leak",
      "68",     "Controlled Burn",
      "69",     "Inspection",
      "70",     "Bomb Threat",
      "74",     "Verbal Warning Issued",
      "75",     "Citation Issued",
      "76",     "Attempted Suicide or Overdose",
      "77",     "N.C.I.C Hit",
      "81",     "Precaution",
      "88",     "Phone Number",
      "97",     "Arrived at Scene",
      "98",     "Finished with Last Assignment",
      "149",    "DUI",
      "Code1",  "Non-Emergency",
      "Code2",  "Lights and No Siren",
      "Code3",  "Lights and Siren",
      "SIG-1",  "Residence or Family Member",
      "SIG-2",  "Meet with...",
      "SIG-3",  "Message or Information",
      "SIG-4",  "Station or Office",
      "SIG-5",  "Eating",
      "SIG-6",  "Call by Phone",
      "SIG-7",  "Officer in Extreme Danger",
      "SIG-8",  "Disregard",
      "SIG-9",  "Rush,Quick Action Desired",
      "SIG-10", "Confidential Information"

  });

  private static final String[] CITY_LIST = new String[]{
      "BRONSTON",
      "BURNSIDE",
      "ETNA",
      "EUBANK",
      "FAUBUSH",
      "FERGUSON",
      "HAYNES KNOB",
      "INGLE",
      "NANCY",
      "OAK HILL",
      "SCIENCE HILL",
      "SHOPVILLE",
      "SOMERSET",
      "VALLEY OAK",
      "WHITE LILY",
      "WOODSTOCK",
  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
     "SOMERSET CO", "SOMERSET",
     "SS",          "SOMERSET",
  });

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[] {
      "HAYNES KNOB",    "SOMERSET",
      "WHITE LILY",     "SOMERSET"
  });
}
