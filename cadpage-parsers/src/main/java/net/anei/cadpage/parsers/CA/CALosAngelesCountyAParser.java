package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;


/**
 * Los Angeles County, CA
 */
public class CALosAngelesCountyAParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile("^(\\w+):[ /]*(?:(\\d\\d/\\d\\d/\\d\\d) )?(\\d\\d:\\d\\d(?::\\d\\d)?)/");
  
  public CALosAngelesCountyAParser() {
    super(CITY_CODES, "LOS ANGELES COUNTY", "CA",
           "ALARM CALL ADDRCITY+? X1 ( X/ZSLS PLACE UNIT MAP ID | PLACE UNIT MAP ID ) INFO2+");
  }
  
  @Override
  public String getFilter() {
    return "Verdugo@VerdugoFire.com,ljensen034m@gmail.com,landon034@icloud.com";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strSource = match.group(1);
    data.strDate = getOptGroup(match.group(2));
    data.strTime = match.group(3);
    body = body.substring(match.end()).trim();
    return parseFields(body.split("/"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC DATE TIME " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ALARM")) return new MyAlarmField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X1")) return new MyCross1Field();
    if (name.equals("ID")) return new IdField("[A-Z]{3}\\d{3,}", true);
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }
 
  @Override
  public String adjustMapAddress(String sAddress) {
    return FWY_PTN.matcher(sAddress).replaceAll("RT $1");
  }
  private static final Pattern FWY_PTN = Pattern.compile("\\b(\\d+) *FWY\\b", Pattern.CASE_INSENSITIVE);
  
  private class MyAlarmField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() != 1) abort();
      char code = field.charAt(0);
      if (code < '0' || code > '9') abort();
      if (field.charAt(0) > '1') {
        data.strCall = "ALARM:" + code;
      }
    }
  }
  
  private class MyCallField extends CallField {
    @Override 
    public void parse(String field, Data data) {
      String desc = CALL_CODES.getProperty(field);
      if (desc != null) {
        data.strPriority = desc.substring(0,1);
        field = desc.substring(2);
      }
      data.strCall = append(field, " ", data.strCall);
    }
    
    @Override
    public String getFieldNames() {
      return "PRI CALL";
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      // If parsed a city field on a previous pass, return failure
      if (data.strCity.length() > 0) return false;
      
      String saveAddr = data.strAddress;
      data.strAddress = "";
      String place = null;
      int pt = field.indexOf(',');
      if (pt >= 0) {
        int pt2 = field.indexOf('(', pt);
        if (pt2 >= 0) {
          place = stripFieldEnd(field.substring(pt2+1), ")");
          field = field.substring(0,pt2).trim();
        }
        pt2 = field.indexOf('<', pt);
        if (pt2 >= 0) field = field.substring(0,pt2).trim();
      }
      if (!super.checkParse(field, data)) parseAddress(field, data);
      data.strAddress = append(saveAddr, " & ", data.strAddress);
      if (place != null) data.strPlace = append(data.strPlace, " - ", place);
      return true;
    }
  }
  
  private static final Pattern X_MARKER = Pattern.compile("^(?:btwn |low xst:)");
  private class MyCross1Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      Matcher match = X_MARKER.matcher(field);
      if (!match.find()) abort();
      field = field.substring(match.end()).trim();
      field = stripFieldEnd(field, "&");
      super.parse(field, data);
    }
  }

  private class MyInfo2Field extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "EMS INFO:");
      field = stripFieldStart(field, "Remarks:");
      data.strSupp = append(data.strSupp, " / ", field);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALH", "ALHAMBRA",
      "ANF", "ANF",
      "ARC", "ARCADIA",
      "BRK", "BURBANK",
      "BUR", "BURBANK AIRPORT",
      "CMP", "COMPTON",
      "DNY", "DOWNEY",
      "GLN", "GLENDALE",
      "LAC", "LOS ANGELES COUNTY",
      "LFD", "LOS ANGELES CITY",
      "MPK", "MONTEREY PARK",
      "MRV", "MONROVIA",
      "MTB", "MONTEBELLO",
      "PAS", "PASADENA",
      "SBG", "SAN GABRIEL",
      "SFS", "SANTA FE SPRINGS",
      "SGB", "SAN GABRIEL",
      "SMD", "SIERRA MADRE",
      "SNM", "SAN MARINO",
      "SPS", "SOUTH PASADENA",
      "VER", "VERNON"

  });
  
  private static Properties CALL_CODES = buildCodeTable(new String[]{
      "ALERT2",      "1/Emergency Landing At Airport",
      "ALERT3",      "1/Plane Accident, Down or Fire",
      "PERSON",      "1/Person On Fire",
      "PLANE",       "1/Plane Accident, Down or Fire",
      "TCSTR",       "1/Vehicle Into A Structure",
      
      "BUILD",       "2/Building Collapse Rescue",
      "CONFIN",      "2/Confined Space Rescue",
      "DERAIL",      "2/Train Derailment",
      "EXPLO",       "2/Explosion Seen Or Heard",
      "HANG",        "2/Person Hanging",
      "JUMPER",      "2/Person Threatening To Jump",
      "MTNRES",      "2/Mountain Rescuer",
      "RESCUE",      "2/Rescue",
      "RESFWY",      "2/TC-Rescue on the FWY",
      "SWR",         "2/Swift Water Rescue",
      "TCRAIL",      "2/Traffic Collision w/Train",
      "TCRES",       "2/Traffic Collision w/Rescue",
      "TRENCH",      "2/Trench Rescue",
      "USAR",        "2/Collapse/Extrication/Rescue",
      "WMD",         "2/Weapons Mass Destruction Event",
      
      "ABD1",        "3/Allergic Reaction",
      "ALERGY",      "3/Allergic Reaction",
      "ALOC",        "3/Altered Level of Consciousness",
      "BITE1",       "3/Animal/Stings – ALS",
      "BP",          "3/Blood Pressure Problem",
      "BREATH",      "3/Shortness of Breath",
      "BURN",        "3/Burn Injury",
      "CHEST",       "3/Chest Pain",
      "CHOKE",       "3/Person Choking",
      "DIAB",        "3/Diabetic Problem",
      "DOWN",        "3/Person Down",
      "DROWN",       "3/Drowning/Diving Accident",
      "EMS",         "3/EMS Call – No Details",
      "EMSFWY",      "3/EMS on the FWY",
      "ENGRA",       "3/Misc Response for 1 RA",
      "EXPOS",       "3/Exposure-Heat or Cold",
      "EYE",         "3/Eye Injury",
      "FALL1",       "3/Fall Major",
      "GIB",         "3/G. I. Bleed",
      "GSW",         "3/Gun Shot Wound",
      "HEART",       "3/Heart Problem",
      "LAW",         "3/PD Assist – Stand By",
      "NOTBR",       "3/Person Not Breathing",
      "OBGYN",       "3/Obstetrics/Gynecological",
      "OD",          "3/Overdose",
      "PEDI",        "3/Pediatrics 0-36 Months",
      "PSYCH",       "3/Psychiatric",
      "SEIZ",        "3/Person In Seizure",
      "SHOCK",       "3/Electrical Shock Injury",
      "STAB",        "3/Person Stabbed",
      "STEMI",       "3/STEMI Hospital Transport",
      "STROKE",      "3/CVA-Neurological",
      "SYNCO",       "3/Syncope / Fainted Now Conscious",
      "TCBIKE",      "3/Traffic Collision w/Bicycle",
      "TCFWY",       "3/Traffic Collision on the FWY",
      "TCMC",        "3/Traffic Collision w/Motorcycle",
      "TCPED",       "3/Traffic Collision w/Pedestrian",
      "TOXIC",       "3/Ingestion-Poisoning-Inhalation",
      "TR1",         "3/Severe Trauma",
      "UNCON",       "3/Person Unconscious-Unresponsve",
      "UNKMED",      "3/Unknown Medical",
      
      "APT",         "4/Apartment House Fire",
      "BRUSH",       "4/Brush Fire",
      "BRUSHF",      "4/Brush Fire (Full Assignment)",
      "GARAGE",      "4/Garage Fire",
      "GMAIN",       "4/Broken Gas Main",
      "HAZMAT",      "4/Hazardous Materials Response",
      "HOUSE",       "4/House Fire",
      "STR",         "4/Structure Fire",
      "VAULT",       "4/Electrical Vault Fire",
      "VEG",         "4/Vegetation Fire",
      "VEGFWY",      "4/Vegetation Fire On The Fwy",
      "VEHSTR",      "4/Vehicle Fire In A Structure",
      "ABD2",        "5/Abdominal Pain – BLS",
      "ASLT",        "5/Assault Victim",
      "BACK",        "5/Back Pain",
      "BITE2",       "5/Animal/Stings – BLS",
      "DIZZY",       "5/Person Dizzy",
      "FALL2",       "5/Fall Minor",
      "HEADPN",      "5/Head Pain",
      "MED",         "5/Misc. Medical",
      "MEDALR",      "5/Medical Alarm",
      "NOSE",        "5/Nose Bleed – Non Traumatic",
      "SICK",        "5/Person Sick",
      "SICK2",       "5/Person Sick (FLU Symptoms)",
      "TC",          "5/Traffic Collision",
      "TCFUEL",      "5/Traffic Collision w/Fuel Spill",
      "TR2",         "5/Minor Trauma",
      "WELFAR",      "5/Investigate The Welfare",
      
      "APPL1",       "6/Appliance Fire – Major",
      "APPL2",       "6/Appliance Fire - Minor",
      "ELECF",       "6/Electrical Fire",
      "FLOW",        "6/Sprinkler System Activated",
      "FULL",        "6/Misc Response Full Assignment",
      "GASI",        "6/Odor of Natural Gas Inside",
      "RAIL",        "6/Train Fire",
      "SMOKEI",      "6/Smoke In A Structure",
      "UNKF",        "6/Unknown Type Fire",
      
      "ALARM",       "7/Fire Alarm",
      "ANIMAL",      "7/Animal Rescue",
      "ARC",         "7/Arcing Wires",
      "BEES",        "7/Bee Investigation",
      "BIO",         "7/Biohazard Investigation",
      "CARBON",      "7/Carbon Monoxide Alarm",
      "CHEMO",       "7/Chemical Outside",
      "CROWD",       "7/Investigate Overcrowding",
      "DAMAGE",      "7/Damage Investigation",
      "ELEV",        "7/Elevator Rescue",
      "ENG",         "7/Misc Response for 1 Engine",
      "ENGTRK",      "7/Misc Response for 1 Trk",
      "FLOOD",       "7/Major Flooded Condition",
      "FNO",         "7/Fire Now Out Investigation",
      "FUEL",        "7/Fuel Spill",
      "FWORKS",      "7/Fireworks Investigation",
      "GASO",        "7/Odor of Natural Gas Outside",
      "HAZARD",      "7/Investigate a Hazardous Condition",
      "HYD",         "7/Flowing/Gushing/Sheared Hydrant",
      "IA",          "7/Initial Action Response",
      "ILLEG",       "7/Illegal Burning",
      "LOCK",        "7/Person Locked In-Locked Out",
      "MALF",        "7/Malfunction",
      "MUD",         "7/Mudslide Investigation",
      "ODOR",        "7/Misc. Odor Investigation",
      "OUT",         "7/Misc. Outdoor Fire",
      "OUTFWY",      "7/Misc. Outside Fire On The Fwy",
      "POLE",        "7/Transformer/Street Light Fire",
      "PUBLIC",      "7/Public Assist",
      "REFRIG",      "7/Abandoned Refrigerator",
      "REFUSE",      "7/Refuse Fire",
      "RESET",       "7/Fire Alarm Reset",
      "ROOF1",       "7/One Story Roof Check",
      "ROOF2",       "7/Two Story Roof Check",
      "SMOKEO",      "7/Smoke In The Area",
      "THREAT",      "7/Bio-Chemical Threat",
      "TRK",         "7/Misc. Response for 1 Trk",
      "VEH",         "7/Vehicle Fire",
      "VEHFWY",      "7/Vehicle Fire on the Fwy",
      "WATER",       "7/Minor Flooding",
      "WIRES",       "7/Wires Down",
      "WMAIN",       "7/Broken Water Main",
      
      "INVL",        "8/Assist The Invalid",
      
      "ALERT1",      "9/Airport Alert 1 - Stand By",
      "BOMB",        "9/Bomb Threat",
      "TCNON",       "9/Non-Inj Traffic Collision"
  });
}
