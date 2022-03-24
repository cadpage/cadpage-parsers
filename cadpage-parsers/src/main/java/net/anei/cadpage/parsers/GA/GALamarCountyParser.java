package net.anei.cadpage.parsers.GA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;


public class GALamarCountyParser extends DispatchA78Parser {

  public GALamarCountyParser() {
    super("LAMAR COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "CAD@ssialerts.com,donotreply@LamarSOalerts.com";
  }

  private static final Pattern CODE_PTN = Pattern.compile("(\\d+)[A-Z]*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    String code = data.strCall;
    data.strCode = code;
    String call = CALL_CODES.getProperty(code);
    if (call == null) {
      Matcher match = CODE_PTN.matcher(code);
      if (match.matches()) {
        code = match.group(1);
        call = CALL_CODES.getProperty(code);
      }
    }
    if (call != null) data.strCall = call;
    return true;
  }

  @Override
  public String getProgram() {
    return "CODE " + super.getProgram();
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "0",  "Caution",
      "1",  "Unable to Copy",
      "2",  "Signal Good",
      "3",  "Stop Transmitting",
      "4",  "Okay",
      "5",  "Relay",
      "6",  "Busy Unless Urgent",
      "7",  "Out of Service",
      "8",  "In Service",
      "9",  "Repeat",
      "10",  "Fight or Disorder",
      "11",  "Dog Complaint",
      "12",  "Stand By",
      "13",  "Weather/Road Report",
      "14",  "Prowler",
      "15",  "Burglary",
      "16",  "Domestic",
      "17",  "Armed Robbery",
      "18",  "Quickly",
      "19",  "Return To",
      "20",  "Location",
      "21",  "Call By Phone",
      "22",  "Disregard",
      "23",  "Arrived at Scene",
      "24",  "Assignment Completed",
      "25",  "Meet In Person",
      "26",  "Detaining Subject",
      "27",  "Driver's License Info",
      "28",  "Vehicle Registration Info",
      "29",  "Check for Stolen or Wanted",
      "30",  "Uncessary Use of Radio",
      "31",  "Crime in Progress",
      "32",  "Subject with Firearm",
      "33",  "Emergency",
      "34",  "Riot",
      "35",  "Foot Patrol",
      "36",  "Correct Time",
      "37",  "Suspicious Person or Vechicle",
      "38",  "Stopping Person or Vechicle",
      "39",  "Resume Normal Opertion",
      "40",  "Check w/owner if not stolen",
      "41",  "Beginning Tour of Duty",
      "42",  "Ending Tour of Duty",
      "43",  "Murder Reported",
      "44",  "Suicide or Attempt",
      "45",  "GBI Crime Lab, evidence",
      "46",  "Assist Motorist",
      "47",  "Emergency Road Repair",
      "48",  "Traffic Light Out",
      "49",  "Speeding Auto",
      "50",  "Accident (Fire, Injured, Dow)",
      "51",  "Wrecker Needed",
      "52",  "Ambulance Needed",
      "53",  "Road Blocked",
      "54",  "Livestock or Carcass on Road",
      "55",  "Intoxicated Driver",
      "56",  "Intoxicated Pedestrain",
      "57",  "Hit and Run",
      "58",  "Direct Traffic",
      "59",  "Convoy of Escort",
      "60",  "Will Leave Station at (time)",
      "61",  "Civil Matter",
      "62",  "Prepare to Make Written Copy",
      "64",  "Alarm Activated",
      "65",  "Mechanical Breakdown",
      "66",  "Suspended License",
      "67",  "Drug Traffic",
      "68",  "Dispatch Information",
      "69",  "Message Received",
      "70",  "Fire",
      "70B", "Brush Fire",
      "70S", "Structure Fire",
      "70V", "Vehicle Fire",
      "71",  "Damage to Property",
      "72",  "Harassing Phone Calls",
      "73",  "DUI Test",
      "74",  "Negative",
      "75",  "In Contact With",
      "76",  "En-Route",
      "77",  "Estimated Time of Arrival (ETA)",
      "78",  "Need Assistance",
      "79",  "Notify Coroner",
      "80",  "Chase, Vehicle/Person",
      "81",  "Give Location and Status",
      "82",  "Reserve Lodging",
      "83",  "School Crossing Duty",
      "84",  "Special Detail",
      "85",  "Delay Due To",
      "86",  "Officer or Operator on Duty",
      "87",  "Pickup and Distribute Checks",
      "88",  "Telephone Number of",
      "89",  "Bomb Threat",
      "90",  "Bank Alarm",
      "91",  "Pick up Subject of Prisioner",
      "92",  "Improperly Parked Vehicle",
      "93",  "Blockade",
      "94",  "Drag Racing",
      "95",  "Prisoner or Subject in Custody",
      "96",  "Mental Subject",
      "97",  "Check Signal",
      "98",  "Prison or Jail Break",
      "99",  "Wanted or Stolen Indicated",
      "100",  "Rape or Reported Rape",
      "101",  "Medical Acknowledgement (OK)",
      "102",  "Send Rescue Unit To",
      "103",  "Send Police Unit To",
      "104",  "Unable to Locate",
      "105",  "Patient Refuses Service",
      "106",  "Patient Refuses Treatment",
      "107",  "Patient Picked up by other means",
      "108",  "Transfer Patient From - To",
      "109",  "Patient Condition",
      "110",  "Multi-Injuy Accident"
  });
}

