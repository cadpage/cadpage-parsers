package net.anei.cadpage.parsers.SC;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class SCHorryCountyParser extends DispatchA52Parser {

  public SCHorryCountyParser() {
    super(CALL_CODES, "HORRY COUNTY", "SC");
  }


  private static final Properties CALL_CODES = buildCodeTable(new String[] {
      "FA",     "Fire Alarm",
      "FAR",    "Animal Rescue",
      "FBA",    "Boat Accident",
      "FBF",    "Brush Fire",
      "FBFB",   "Brush Fire Near Building",
      "FBOAT",  "Boat Fire",
      "FBOM",   "Bomb Threat",
      "FCARD",  "Cardiac Arrest",
      "FCB",    "Control Burn",
      "FCO",    "Carbon Monoxide",
      "FCSR",   "Confined Space Rescue",
      "FCV",    "Code Violation",
      "FDF",    "Dumpster Fire",
      "FEF",    "Electrical Fire",
      "FELEV",  "Elevator Entrapment",
      "FGL",    "Gas Leak",
      "FHAZM",  "Hazardous Material",
      "FIB",    "Illegal Burning",
      "FLA",    "Lift Assist",
      "FLO",    "Lockout",
      "FLZ",    "Landing Zone",
      "FMA",    "Mutual Aid request",
      "FPA",    "Public Assistance",
      "FPF",    "Pole Fire",
      "FPLC",   "Plane Crash",
      "FPLD",   "Plane in Distress",
      "FR",     "First Responder",
      "FSBA",   "Standby at Airport",
      "FSD",    "Smoke Detector Install",
      "FSF",    "Structure Fire",
      "FSHC",   "Structure Fire Commercial/High Rise",
      "FSIA",   "Smoke in Area",
      "FTA",    "Traffic Accident",
      "FTAE",   "Traffic Accident Entrapment",
      "FTDR",   "Tree Down in Roadway",
      "FVF",    "Vehicle Fire",
      "FVFB",   "Vehicle Fire Near Building",
      "FWD",    "Wires Down",
      "FWFA",   "Water Flow Alarm",
      "FWR",    "Water Rescue",
      "FWSO",   "Water Shut Off"

  });

}
