
package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Jefferson County, AL (D)
 */
public class ALJeffersonCountyDParser extends FieldProgramParser {
  

  public ALJeffersonCountyDParser() {
    super("JEFFERSON COUNTY", "AL",
          "ADDRCITY CODE! X:X! Units:UNIT! Created:DATETIME! ID! N:INFO/N+");
  }
    
  @Override
  public String getFilter() {
    return "dispatch@cob.org,Dispatch@birminghamal.gov";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch") && !subject.equals("!")) return false;
    return parseFields(body.split("\n"), 5, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ID")) return new IdField("PriInc *(.*)");
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("Address")) abort();
      field = field.substring(7).trim();
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      field = field.replace('@', '&');
      parseAddress(field, data);
    }
  }
  
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("Calltype")) abort();
      field = field.substring(8).trim();
      data.strCode = field;
      data.strCall = convertCodes(field, CALL_CODES);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M) *#");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }
  
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "AA",   "Asthma Attack",
      "AB",   "Animal Bite",
      "AIR",  "Air Conditioner",
      "AOE",  "Assist Occupant (hands on)",
      "AOF",  "Assist occupant ( hands off )",
      "APT",  "Apartment Fire",
      "APTE", "Apartment Fire with Injuries",
      "AR",   "Allergic Reaction",
      "ASLT", "Assault",
      "ASP",  "Assist Police",
      "AUPT", "Automobile Person Trapped",
      "AUTI", "Automobile on Interstate",
      "AUTO", "Automobile",
      "BE",   "Battery Explosion",
      "BHF",  "Boarding Home Fire",
      "BHOT", "Heat Related EMS",
      "BI",   "Back Injury",
      "BIKE", "Bicyclist Struck",
      "BT",   "Bomb Threat",
      "Burn", "Burn Scald",
      "BUS",  "Bus Fire",
      "CA",   "Cardiac Arrest",
      "CCH",  "Child Choking",
      "CF",   "Commercial Fire",
      "CMD",  "Carbon Monoxide Detector",
      "CP",   "Chest Pains",
      "CS",   "Child Struck",
      "CSR",  "Confine Space Rescue",
      "CUT",  "Cut - Minor",
      "CUTM", "Cut- Major",
      "CVA",  "Stroke",
      "DB",   "Difficulty Breathing",
      "DECK", "Parking Deck (Auto)",
      "DI",   "Diabetic",
      "DISH", "Dishwasher",
      "DRN",  "Drowning",
      "DRY",  "Dryer",
      "DUMP", "Dumpster Fire",
      "DUP",  "Duplicate Call",
      "EF",   "Electrical Failure",
      "EL",   "Electrocution",
      "EMSA", "All Medicals to Airport",
      "EMSI", "Medical on Interstate",
      "EMSM", "Med-Major",
      "ER",   "Elevator Rescue",
      "EYE",  "Eye Injury",
      "FALL", "Fall Minor",
      "FALM", "Fall Major",
      "FB",   "Fuse box",
      "FDA",  "Fire Department Accident",
      "FFI",  "Fire Fighter Injury",
      "FS",   "Fuel Spill",
      "FSI",  "Fuel Spill on Interstate",
      "FURN", "Furnace",
      "FX",   "Fracture",
      "GF",   "Garage Fire",
      "GRASS","Grass Fire",
      "GRSI", "Grass on Interstate",
      "HA",   "Heart Attack",
      "HEM",  "Hemorrhage",
      "HER",  "High Elevation Rescue",
      "HF",   "House Fire",
      "HI",   "Head Injury",
      "High", "High Rise",
      "HP",   "Heart Patient",
      "HPT",  "House Person Trapped",
      "HS",   "Heat Stroke",
      "HYPO", "Hypothermia",
      "HZIH", "Tanker spill over 100 gal.",
      "HZIS", "Spill in Structure",
      "HZM",  "Tanker Spill up to 100 gal.",
      "HZOA", "Spill in Open Area",
      "HZRR", "Rail  Spill Fire",
      "HZSF", "Storage Tank Fire",
      "HZST", "Storage Tank Release",
      "HZTF", "Tanker Fire",
      "HZUN", "Hazmat Unknown",
      "IA",   "Industrial Accident",
      "IFO",  "Investigate Fire Reported Out",
      "IN",   "Investigate-Misc.",
      "INA",  "Investigate Alarm",
      "INAM", "Hospitals/Schools",
      "IND",  "Industrial Complex",
      "INFS", "Investigate Fuel Spill",
      "LFX",  "Light Fixture",
      "LS",   "Lighting  Strike",
      "MA",   "Medical Alarm",
      "MACE", "Mace Victim",
      "MC",   "Mass Casualty Incident",
      "MED",  "Medical Aid Minor",
      "MEDC", "Medical aid on Child",
      "MH",   "Motor Home",
      "MOBF", "Mass Occupant Building Fire",
      "MUA",  "Mutual Aid",
      "MVA",  "Motor Vehicle Accident",
      "MVI",  "MVA on Interstate",
      "NB",   "Nose Bleed",
      "NGLK", "Natural Gas Leak",
      "NON",  "Non-Response Call",
      "OB",   "Child Birth",
      "OBLD", "Out Building Fire",
      "OD",   "Overdose",
      "ODOR", "Investigate –Misc.",
      "PC",   "Person Choking",
      "PD",   "Person Down",
      "POIS", "Poisoning",
      "PS",   "Pedestrian Struck",
      "PT",   "Person Trapped",
      "PUC",  "Person Unconscious",
      "RA",   "Respiratory Arrest",
      "RR",   "Ring Removal",
      "RRC",  "Railroad Car Fire",
      "SA",   "Smoke Alarm",
      "SCHF", "School Fire",
      "SNKB", "Snake Bite ",
      "SPK",  "Sprinkler",
      "STAB", "Stabbing",
      "STOV", "Stove Fire",
      "SUI",  "Attempted Suicide",
      "SZ",   "Seizure",
      "THF",  "Town Home Fire",
      "TP",   "Tar Pot",
      "TRAN", "Transformer",
      "TREE", "Tree on Fire",
      "TRK",  "Truck Fire",
      "TRKI", "Truck Fire on Interstate",
      "TRSH", "Trash",
      "TS",   "Tree on Structure",
      "UNK",  "Unknown Medical",
      "WASH", "Washing Machine",
      "WF",   "Wreck with Fire",
      "WH",   "Water Heater",
      "WI",   "Wiring Inside",
      "WM",   "Wreck Involving Motorcycle",
      "WO",   "Wiring Outside ",
      "WOOD", "Woods Fire",
      "WPT",  "Wreck Person Trapped",
      "WR",   "Water Rescue",
      "WSI",  "With Spinal Injury",
      "XFER", "Transfer to Another Agency"
  });
}
