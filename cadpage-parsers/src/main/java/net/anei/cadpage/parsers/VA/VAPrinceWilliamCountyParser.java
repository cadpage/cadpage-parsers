package net.anei.cadpage.parsers.VA;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


/**
 * Prince William County, VA
 */
public class VAPrinceWilliamCountyParser extends FieldProgramParser {

  private static final String TRAIL_MARKER = "\nSent by PW Alert to ";
  private static final Pattern BREAK_PTN = Pattern.compile("(\n|(?<!\\s) *(?=Event Type:|Inc. Address:|Box Number:|Radio Channel:|Command:|Landing Zone:|Comments:))");

  public VAPrinceWilliamCountyParser() {
    super(CITY_CODES, "PRINCE WILLIAM COUNTY", "VA",
          "( SELECT/1 DATE1 TIME1 CODE ADDR1 X1/Z+? MAP1 UNIT! INFO1+ | " +
            "Event_Type:CALL! Inc._Address:ADDR2! Box_Number:BOX! Radio_Channel:CH! Command:UNIT! Landing_Zone:LZ2? Comments:INFO2 )");
  }

  @Override
  public String getFilter() {
    return "cc_message_notification@usamobility.net,@rsan.pwcgov.org,@everbridge.net,PWRSAN,87844,88911,89361";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // FIrst, look for a trailing data marker and strip it off
    boolean good = false;
    int pt = body.indexOf(TRAIL_MARKER);
    if (pt < 0) {
      pt = body.lastIndexOf('\n');
      if (pt >= 0) {
        if (!TRAIL_MARKER.startsWith(body.substring(pt))) pt = -1;
      }
    }
    if (pt > 0) {
      good = true;
      body = body.substring(0,pt).trim();
    }

    String save = body;

    if (subject.endsWith("FINAL")) data.msgType = MsgType.RUN_REPORT;

    // We have two different page formats, one slash separated
    // and one newline separated

    String[] flds = body.replace('\n', ' ').split("/");
    if (flds.length >= 6) {
      setSelectValue("1");
      if (parseFields(body.split("/"), 6, data)) return true;
    }
    else {
      setSelectValue("2");
      if (!body.startsWith("Event Type:")) body = "Event Type: " + body;
      if (parseFields(BREAK_PTN.split(body), 5, data)) return true;
    }

    if (!good) return false;

    if (subject.length() > 0) {
      save = '(' + subject + ") " + save;
    }
    data.parseGeneralAlert(this, save);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CALL")) return new MyCallField();

    if (name.equals("DATE1")) return new MyDateField();
    if (name.equals("TIME1")) return new MyTimeField();
    if (name.equals("ADDR1")) return new MyAddressField();
    if (name.equals("X1")) return new MyCrossField();
    if (name.equals("MAP1")) return new MapField("\\d\\d[A-Z]?|[A-Z]{2}|\\d \\d", true);
    if (name.equals("INFO1")) return new MyInfoField();

    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("LZ2")) return new MyLZField();
    if (name.equals("INFO2")) return new MyInfo2Field();

    return super.getField(name);
  }

  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      data.strCall = convertCodes(field, CALL_CODES);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      super.parse(convertCodes(field, CALL_CODES), data);
    }
  }

  private static final DateFormat DATE_FMT = new SimpleDateFormat("dd-MMM-yyyy");
  private class MyDateField extends DateField {
    @Override
    public void parse(String field, Data data) {
      setDate(DATE_FMT, field, data);
    }
  }

  private class MyTimeField extends TimeField {
    public MyTimeField() {
      setPattern("\\d\\d:\\d\\d:\\d\\d", true);
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String sPlace = append(p.getOptional('@'), " - ", p.getLastOptional('('));
      if (sPlace.endsWith(")")) sPlace = sPlace.substring(0, sPlace.length()-1).trim();
      data.strPlace = sPlace;
      String city = convertCodes(p.getLastOptional(','), CITY_CODES);
      int pt = city.indexOf(',');
      if (pt >= 0) {
        data.strState = city.substring(pt+1);
        city = city.substring(0,pt);
      }
      data.strCity = city;
      parseAddress(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        if (data.strCity.length() == 0) data.strCity = convertCodes(field.substring(pt+1).trim(), CITY_CODES);
        field = field.substring(0,pt).trim();
      }

      // very occasionally, the address will be a cross street and the
      // cross street will be an address
      if (data.strCross.length() == 0 && field.length() > 0 && Character.isDigit(field.charAt(0)) &&
          (data.strAddress.length() == 0 || !Character.isDigit(data.strAddress.charAt(0)))) {
        data.strCross = data.strAddress;
        data.strAddress = "";
        parseAddress(field, data);
      } else {
        super.parse(field, data);
      }
    }
  }

  private static final Pattern ID_PTN = Pattern.compile("\\[(\\d+)]?$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_PTN.matcher(field);
      if (match.find()) {
        data.strCallId = match.group(1);
        field = field.substring(0,match.start()).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO ID";
    }
  }

  private static final Pattern PAREN_PTN = Pattern.compile("\\(.*\\)");
  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      super.parse(PAREN_PTN.matcher(field).replaceAll("").trim(), data);
    }
  }

  private class MyLZField extends MyInfoField {
    @Override
    public void parse(String field, Data data) {
      super.parse("LZ: " + field, data);
    }
  }

  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return TN_PTN.matcher(sAddress).replaceAll("TURN");
  }
  private static final Pattern TN_PTN = Pattern.compile("\\bTN\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
    "ACCA",     "Motor Vehicle Accident/ALS",
    "ACCAIR",   "Aircraft Crash/Fire/Distress/Leak",
    "ACCX",     "Auto Accident with Entrapment",
    "ACCB",     "Motor Vehicle Accident/BLS",
    "APPC",     "Appliance Fire in a Commercial Building",
    "AIRHAZ",   "Aircraft Hazard",
    "APPA",     "Appliance Fire in an Apartment",
    "APPH",     "Appliance Fire in an House",
    "APPT",     "Appliance Fire in an Townhouse",
    "APT",      "Apartment Fire",
    "ASSA",     "ALS - Assault",
    "ASSB",     "BLS - Assault",
    "ATTA",     "ALS - Attempted Suicide",
    "ATTB",     "BLS - Attempt Suicide",
    "BLDGA",    "Building Collapse Apartment",
    "BLDGC",    "Building Collapse in a Commercial Building",
    "BLDGCOM",  "Building Collapse Commercial",
    "BLDGH",    "Building Collapse Single Family Dwelling",
    "BLDGT",    "Building Collapse in a Townhouse",
    "BLDGTH",   "Building Collapse Townhouse",
    "BOATL",    "Boat Fire Lake",
    "BOATR",    "Boat Fire River",
    "BOMBF",    "Bomb Threat requiring the Fire Board",
    "BRUSH",    "Brush Fire",
    "CHIM",     "Chimney Fire",
    "CHOA",     "ALS - Choking",
    "CHOB",     "BLS - Choking",
    "CO",       "Carbon Monoxide Alarm Sounding",
    "COMM",     "Commercial Fire",
    "CSPACE",   "Confined Space",
    "DROA",     "ALS - Drowning",
    "DROB",     "BLS - Drowning",
    "ELEV",     "Elevator Rescue",
    "ESERV",    "EMS Service Call",
    "ESERVICE", "BLS - EMS Service",
    "FALARM",   "Fire Alarm Sounding",
    "FOUT",     "Fire Reported Out",
    "FSERV",    "Fire Service Call",
    "FSERVICE", "Fire Service",
    "HANGLE",   "High Angle Rescue",
    "HAZARD",   "Hazardous Situation",
    "HAZMAT",   "HazMat Incident",
    "HOUSE",    "House Fire",
    "INGASA",   "Inside Gas Leak Apartment",
    "INGASC",   "Inside Gas Leak Commercial Building",
    "INGASH",   "Inside Gas Leak Single Family Dwelling",
    "INGAST",   "Inside Gas Leak Townhouse",
    "INJA",     "ALS - Injury",
    "INJB",     "BLS - Injury",
    "INVEST",   "Investigation",
    "INVESTIG", "Investigation",
    "LAKRES",   "Lake Rescue",
    "LOCK",     "Lockout",
    "LOCKOUT",  "Lockout",
    "MALA",     "Medical Alarm Sounding",
    "ODA",      "ALS - Overdose",
    "ODB",      "Overdose BLS",
    "OUTF",     "Outside Fire",
    "OUTGAS",   "Outside Odor of Gas",
    "RESCUE",   "Rescue Incident Non-Vehiclular",
    "RIVRES",   "River Rescue",
    "SICA",     "ALS - Sickness",
    "SICB",     "BLS - Sickness",
    "SMELL",    "Odor of Smoke",
    "STOP",     "Stoppage Of Breathing",
    "STRUCT",   "Structure Fire - All Types",
    "STRUCT-2", "Structure Fire - 2nd Alarm or Greater",
    "SWRES",    "Swift Water Rescue",
    "TH",       "Townhouse Fire",
    "THR",      "BLS - Threatened Suicide",
    "TRANSF",   "Mutual Aid Transfer",
    "TRANSP",   "Emergency Transport",
    "TRENCH",   "Trench Space",
    "TRUCK",    "Vehicle Fire (large vehicles)",
    "UALARM",   "Unknown Alarm Sounding",
    "UNC",      "Unconscious",
    "UNKSIT",   "Unknown Situation",
    "VEH",      "Vehicle Fire (small vehicles)"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CHAR", "CHARLES COUNTY,MD",
      "DULL", "DULLES",
      "DUMF", "DUMFRIES",
      "FAIR", "FAIRFAX",
      "FAUQ", "FAUQUIER",
      "FBEL", "FORT BELVOIR",
      "FXCT", "FAIRFAX",
      "HAYM", "HAYMARKET",
      "LOUD", "LOUDOUN",
      "MCB",  "MARINE CORPS BASE QUANTICO",
      "MNPK", "MANASSAS PARK",
      "MNSS", "MANASSAS",
      "OCCO", "OCCOQUAN",
      "PWC",  "",
      "QUAN", "QUANTICO",
      "STAF", "STAFFORD",
      "WAR",  "WARRENTON"
  });
}