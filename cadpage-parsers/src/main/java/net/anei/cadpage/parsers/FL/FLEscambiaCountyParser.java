package net.anei.cadpage.parsers.FL;
import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.StandardCodeTable;



public class FLEscambiaCountyParser extends GroupBestParser {

  public FLEscambiaCountyParser() {
    super(new FLEscambiaCountyAParser(), new FLEscambiaCountyBParser());
  }

  static final CodeTable CALL_CODES = new StandardCodeTable(
      "A",      "LAUNCH PARAMOUNT MEDICAL",
      "A2",      "ALERT 2 AIRCRAFT IN TROUBLE",
      "A3",      "ALERT 3 AIRCRAFT CRASH",
      "AA",      "ADMINISTRATIVE ACTIVITY",
      "ACO",     "ANIMAL CONTROL OFFICER",
      "ADMIN",   "ADMIN ACTIVITY UNDEFINED",
      "AG",      "AIR GAS",
      "AIR",     "AIRPORT TRANSFER",
      "APPT",    "SCHEDULED APPOINTMENT",
      "BA",      "BILLABLE FIRE ALARM",
      "C",       "CLINIC",
      "CA",      "CITIZEN ASSIST",
      "CCE",     "CRITICAL CARE EMERGENCY",
      "COM",     "COMPUTER ISSUES",
      "DECON",   "DECONTAMINATE UNIT/EQUIPMENT/CREW",
      "DEPO",    "DEPOSITION",
      "EMS",     "UNKNOWN/NO NATURE MEDICAL CALL",
      "EQ",      "GETTING EQUIPMENT",
      "F",       "LAUNCH PARAMOUNT FIRE",
      "FC",      "FLOODING CONDITIONS",
      "FE",      "FORCED ENTRY",
      "FIR",     "STANDBY AT FIRE",
      "FIRE",    "UNKNOWN/NO NATURE FIRE CALL",
      "FUEL",    "CRITICAL FUEL LEVEL",
      "HFA",     "HURRICANE FIRE ALPHA",
      "HFB",     "HURRICANE FIRE BRAVO",
      "HFC",     "HURRICANE FIRE CHARLIE",
      "HFD",     "HURRICANE FIRE DELTA",
      "HFE",     "HURRICANE FIRE ECHO",
      "HMA",     "HURRICANE MEDICAL ALPHA",
      "HMB",     "HURRICANE MEDICAL BRAVO",
      "HMC",     "HURRICANE MEDICAL CHARLIE",
      "HMD",     "HURRICANE MEDICAL DELTA",
      "HME",     "HURRICANE MEDICAL ECHO",
      "HS",      "HELICOPTER STANDBY",
      "LAB",     "LAB CORP",
      "M",       "MEETING",
      "ME",      "MEDICAL CALL FOR FIRE",
      "MEC",     "MECHANICAL ISSUE",
      "ML",      "METH LAB",
      "MODEM",   "MODEM ISSUE",
      "MP",      "MANPOWER",
      "MUT",     "MUTUAL AID - OUT OF COUNTY",
      "OOT",     "OUT OF TOWN TRANSFER",
      "OS",      "ON SCENE",
      "PA",      "LAUNCH PQA  MED",
      "PF",      "LAUNCH PQA FIRE",
      "PR",      "PUBLIC RELATIONS",
      "PS",      "PRECAUTIONARY STANDBY",
      "REFLASH", "REFLASH RECENT FIRE",
      "SO",      "SPECIAL OPS",
      "STAT",    "STAT TRANSFER",
      "STBY",    "DEDICATED EVENT STANDBY",
      "STR",     "STRETCHER ISSUE",
      "SUP",     "CREW WITH SUPERVISOR",
      "SWT",     "SWAT TEAM STANDBY",
      "TEST",    "TEST CALL AMBULANCE",
      "TEST-FD", "TEST CALL FOR FIRE",
      "TOR",     "TORNADO TOUCHDOWN",
      "TR",      "TRAINING",
      "TRF",     "TRANSFER",
      "TRN",     "TRAINING / CLASS",
      "TXT",     "TEXT TO 911 CALL",
      "WH",      "WAREHOUSE/STOCK CLERK"
  );
}
