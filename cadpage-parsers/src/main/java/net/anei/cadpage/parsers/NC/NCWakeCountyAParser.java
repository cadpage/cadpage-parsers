package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;



public class NCWakeCountyAParser extends FieldProgramParser {

  public NCWakeCountyAParser() {
    super(CITY_CODES, "WAKE COUNTY", "NC",
           "Inc:CALL! Map:MAP! Add:ADDR! Loc:PLACE! Apt:APT! CS:X? Unt:UNIT! TG:CH! Cty:CITY! Comm:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "wcps@wakegov.com,wcps@wake.gov";
  }

  private static final Pattern INFO_ADDR_MARK_PTN = Pattern.compile("\\. : [A-Z]{4}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("WCPS")) return false;
    if (!parseFields(body.split("\n"), data)) return false;

    // See if this is an OOC mutual aid call with an odd address convention
    // if it is, try to extract the real address from the comments.
    String county = stripFieldEnd(data.strCity, " COUNTY");
    if (county.length() < data.strCity.length()) {
      if (data.strAddress.endsWith(' ' + county + " CO WAY")) {
        String[] parts = data.strSupp.split(" / ");
        for (int ii = 0; ii<parts.length-1; ii++) {
          if (INFO_ADDR_MARK_PTN.matcher(parts[ii].trim()).matches()) {
            Result res = parseAddress(StartType.START_OTHER, FLAG_NO_IMPLIED_APT | FLAG_IGNORE_AT | FLAG_NO_CITY, parts[ii+1].trim());
            if (res.isValid()) {
              data.strPlace = data.strAddress;
              res.getData(data);
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(":")) field = field.substring(0,field.length()-1).trim();
      String call = CALL_CODES.getCodeDescription(field);
      if (call != null) {
        data.strCode = field;
        field = call;
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(",", "");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll("  +", " ");
      if (field.startsWith("/")) field = field.substring(1).trim();
      if (field.endsWith("/")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(",")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("\\(1\\).*? LAT: *(\\S*) +LON: *(\\S*?)(?=\\([23])");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.lookingAt()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        field = field.substring(match.end()).trim();
        int pt = field.indexOf("(3)");
        if (pt >= 0) {
          field = field.substring(pt+3).trim();
        } else return;
      } else if (field.contains(" LAT:")) return;

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames();
    }
  }

  @Override
  public String adjustMapCity(String city) {
    city = convertCodes(city.toUpperCase(), MAP_CITY_TABLE);
    return super.adjustMapCity(city);
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable(

      "AIRCRAFTD",  "AIRCRAFT DOWN",
      "AIRCRAFTEM", "AIRCRAFT EMERGENCY",
      "AIRCRAFTSE", "AIRCRAFT INVESTIGATION",
      "ARCINJ",     "ARCING WIRES SING INJ",
      "ARCINJS",    "ARCING WIRES MULT INJS",
      "ARCWIRE",    "ARCING WIRES",
      "ASSIST",     "ASSIST OTHER AGENCY",
      "BOMBT",      "BOMB THREAT",
      "BOMBTHREAT", "BOMB THREAT",
      "BRUSHLG",    "BRUSH FIRE - LARGE",
      "BRUSHSM",    "BRUSH FIRE - SMALL",
      "COALARM",    "CARBON MONOXIDE ALARM",
      "COALMINJ",   "CARBON MONOXIDE ALARM SING INJ",
      "COALMINJS",  "CARBON MONOXIDE ALARM MULT INJ",
      "CODE",       "CODE BLUE",
      "CONTRBURN",  "CONTROLLED BURN",
      "DEATH",      "DECEASED PERSON",
      "EEXPLBOX",   "EMS EXPLOSION BOX",
      "EEXPLHLH",   "EMS EXPLOSION HLH",
      "EEXPLHRF",   "EMS EXPLOSION HRF",
      "ELECINV",    "ELECTRICAL INVESTIGATION",
      "ELECINVINJ", "ELECTRICAL INV SING INJ",
      "ELECINVMUL", "ELECTRICAL INV MULT INJS",
      "ELEVATOR",   "ELEVATOR RESCUE",
      "ENTRAPMENT", "CONFINED SPACE",
      "ESCALATOR",  "ESCALATOR RESCUE",
      "EXPLBOX",    "EXPLOSION FIRE/INJS",
      "EXPLCOMBLD", "EXPLOSION COMMERCIAL BUILDING",
      "EXPLCOMINJ", "EXPLOSION COMM BUIL FIRE/INJS",
      "EXPLCOMVEH", "EXPLOSION COMMERCIAL VEHICLE",
      "EXPLCVEINJ", "EXPLOSION COMM VEH FIRE/INJS",
      "EXPLGBINJS", "EXPLOSION GOV BUIL FIRE/INJS",
      "EXPLGOV",    "EXPLOSION GOVERNMENT BUILDING",
      "EXPLHLFINJ", "EXPLOSION HIGH LIFE FIRE/INJS",
      "EXPLHLH",    "EXPLOSION HIGH LIFE HAZARD",
      "EXPLHR",     "EXPLOSION HIGH RISE",
      "EXPLHRINJS", "EXPLOSION HIGH RISE FIRE/INJS",
      "EXPLMH",     "EXPLOSION MOBILE HOME",
      "EXPLMOBINJ", "EXPLOSION MOBILE H FIRE/INJS",
      "EXPLMR",     "EXPLOSION MULTIPLE RESIDENTIAL",
      "EXPLMULINJ", "EXPLOSION MULT RES FIRE/INJS",
      "EXPLOSION",  "EXPLOSION",
      "EXPLOTHVEH", "EXPLOSION OTHER VEHICLE",
      "EXPLRESD",   "EXPLOSION RESIDENTIAL",
      "EXPLRESINJ", "EXPLOSION RESID FIRE/INJS",
      "EXPLSE",     "EXPLOSION SINGLE ENGINE",
      "EXPLSEINJS", "EXPLOSION SINGLE ENG FIRE/INJS",
      "EXPLVEHINJ", "EXPLOSION OTHER VEH FIRE/INJS",
      "EXTINGFIRE", "EXTINGUISHED FIRE",
      "F1",         "ADDRESS CHECK",
      "F102",       "SHOOTING",
      "F25",        "SIGNAL 25",
      "F50AC",      "AIRCRAFT ACCIDENT",
      "F50BI",      "BOAT ACCIDENT/INJURIES",
      "F50BUS",     "ACCIDENT-BUS",
      "F50D",       "VEHICLE ACCIDENT/DAMAGE ONLY",
      "F50I",       "10-50/INJURIES",
      "F50IC",      "10-50/INJURIES COUNTY ONLY",
      "F50IP",      "10-50/INJURIES/PINNED",
      "F50MC",      "10-50/INJURIES/MOTORCYCLE",
      "F50OD",      "10-50/OVERTURNED/DAMAGE",
      "F50OI",      "10-50/OVERTURNED/INJURIES",
      "F50OP",      "10-50/OVERTURNED/PINNED/INJURIES",
      "F50P",       "10-50/PEDESTRIAN STRUCK",
      "F50TI",      "10-50/TRAIN/INJURIES",
      "F57OD",      "10-57/OVERTURNED/DAMAGE",
      "FA",         "FIRE ALARM",
      "FALM2",      "RFD SECOND ALARM",
      "FALM3",      "RFD THIRD ALARM",
      "FALM4",      "RFD FOURTH ALARM",
      "FALM5",      "RFD FIFTH ALARM",
      "FAME",       "MEDICAL ALARM - FIRE",
      "FAR",        "FIRE ALARM RESIDENTIAL",
      "FASLT",      "ASSAULT",
      "FBD",        "RESPIRATORY DISTRESS",
      "FBURNS",     "SUBJECT BURNED",
      "FC7",        "DECEASED PERSON",
      "FCAD",       "CAD DOWN/TEST",
      "FCHOKE",     "SUBJECT CHOKING",
      "FCIF",       "CHECK IN WITH FIRE",
      "FCODE",      "CODE BLUE",
      "FCVA",       "CVA/STROKE",
      "FDIAB",      "DIABETIC CRISIS",
      "FDROWN",     "DROWNING",
      "FEXPOS",     "EXPOSURE HEAT/COLD",
      "FHE",        "HAZARDOUS EXPOSURE",
      "FHEART",     "CARDIAC",
      "FHEMS",      "HEMORRHAGE SERIOUS",
      "FIPO",       "INJURED PERSON - OTHER",
      "FIPS",       "INJURED PERSON SERIOUS",
      "FMED",       "MEDICAL NATURE UNKNOWN",
      "FMESS",      "MESSAGE ONLY",
      "FNOTI",      "NOTIFICATION ONLY",
      "FOBF",       "OBSTETRICS",
      "FOD",        "OVERDOSE",
      "FPOIS",      "POISONING",
      "FPSYC",      "PSYCHIATRIC",
      "FRDU1",      "FIRST ALARM - RDU",
      "FRDU2",      "SECOND ALARM - RDU",
      "FRDU3",      "THIRD ALARM - RDU",
      "FRDU4",      "FOURTH ALARM - RDU",
      "FRFS",       "FIRE REQUEST FOR SERVICE",
      "FSEIZ",      "SEIZURE",
      "FSHOCK",     "ELECTROCUTION",
      "FSTAB",      "STABBING",
      "FTEST",      "TEST",
      "FUELL",      "FUEL SPILL LARGE",
      "FUELLINJ",   "FUEL SPILL LARGE SING INJ",
      "FUELLINJS",  "FUEL SPILL LARGE MULT INJS",
      "FUELO",      "FUEL ODOR",
      "FUELOINJ",   "FUEL ODOR SING INJ",
      "FUELOINJS",  "FUEL ODOR MULT INJS",
      "FUELS",      "FUEL SPILL SMALL",
      "FUELSINJ",   "FUEL SPILL SMALL SING INJ",
      "FUELSINJS",  "FUEL SPILL SMALL MULT INJS",
      "FUELSTOR",   "FUEL STORAGE FIRE",
      "FUNCON",     "UNCONSCIOUS/FAINTING",
      "FWALK",      "WALK IN",
      "FWF",        "WORKING FIRE",
      "FWHF",       "WORKING HIGH RISE FIRE",
      "FWHF2",      "HIGH RISE FIRE 2ND ALARM",
      "FWHF3",      "HIGH RISE FIRE 3RD ALARM",
      "FWHF4",      "HIGH RISE FIRE 4TH ALARM",
      "FWHF5",      "HIGH RISE FIRE 5TH ALARM",
      "GASIN",      "GAS LEAK IN RESIDENCE",
      "GASL",       "GAS LEAK",
      "GASLINJ",    "GAS LEAK SING INJ",
      "GASLINJS",   "GAS LEAK MULT INJS",
      "GASOCOMMLI", "GAS LEAK OUTSIDE COMM LINE",
      "GASODOR",    "GAS ODOR IN AREA",
      "GASOINJ",    "GAS ODOR SING INJ",
      "GASOINJS",   "GAS ODOR MULT INJS",
      "GASORESDLI", "GAS LEAK OUTSIDE RESD LINE",
      "HAZMAT",     "SM HAZMAT INCIDENT SMALL",
      "HIGHANGLE",  "HIGH ANGLE RESCUE",
      "HOSTAGE",    "HOSTAGE SITUATION",
      "HRF",        "STRUCTURE FIRE HIGH RISE",
      "LIGHTINJ",   "LIGHTNING STRIKE SING INJ",
      "LIGHTINJS",  "LIGHTNING STRIKE MULT INJS",
      "LIGHTNING",  "LIGHTNING STRIKE",
      "MARFSTRINJ", "MARINE FIRE STRUC SING INJ",
      "MARINEF",    "MARINE FIRE",
      "MARINEFINJ", "MARINE FIRE SING INJ",
      "MARINEINJS", "MARINE FIRE MULT INJS",
      "MARINESE",   "MARINE FIRE SINGLE ENGINE",
      "MARINSTRUC", "MARINE FIRE STRUCTURE THREAT",
      "MARSTRINJS", "MARINE FIRE STRUC MULT INJS",
      "MUTAID",     "MUTUAL AID",
      "ODOR",       "ODOR INVESTIGATION",
      "ODORINJ",    "ODOR INVESTIGATION SING INJ",
      "ODORINJS",   "ODOR INVESTIGATION MULT INJS",
      "OTHER",      "OTHER CALL - EXPLAIN",
      "OUTFIRELG",  "OUTSIDE FIRE LARGE",
      "OUTSFINJ",   "OUTSIDE FIRE SING INJ",
      "OUTSFINJS",  "OUTSIDE FIRE MULT INJS",
      "OUTSIDEF",   "OUTSIDE FIRE",
      "OUTSIDEFHM", "OUTSIDE FIRE WITH HAZMAT",
      "RFS",        "REQUEST FOR SERVICE - EXPLAIN",
      "SFICKF",     "SICK CALL FIRST RESPONDER",
      "SHOOT",      "SHOOTING - PERSON SHOT",
      "SLIMC",      "CHILD LOCKED IN A VEHICLE",
      "SMOKEINV",   "SMOKE INVESTIGATION",
      "STAB",       "STABBING",
      "STRUC",      "STRUCTURE FIRE RESIDENTIAL",
      "STRUCAPPL",  "STRUCTURE FIRE APPLIANCE",
      "STRUCCHIM",  "STRUCTURE FIRE CHIMNEY",
      "STRUCCOLL",  "ENTRAPMENT STRUCTURE COLLAPSE",
      "STRUCCOMM",  "STRUCTURE FIRE COMMERCIAL",
      "STRUCCOMMH", "STRUCTURE FIRE COMMERCIAL HM",
      "STRUCHLH",   "STRUCTURE FIRE HIGH LIFE HAZAR",
      "STRUCLND",   "STRUCTURE FIRE LARGE NON DWELL",
      "STRUCMH",    "STRUCTURE FIRE MOBILE HOME",
      "STRUCMR",    "STRUCTURE FIRE MULTIPLE RESID",
      "STRUCSND",   "STRUCTURE FIRE SMALL NON DWELL",
      "STRUCWATER", "STRUCTURE FIRE OVER WATER",
      "SUBJFIRE",   "SUBJECT ON FIRE",
      "SUSPPACK",   "SUSPICIOUS PACKAGE",
      "TRAINDERAI", "TRAIN DERAILMENT",
      "TRAINF",     "TRAIN FIRE",
      "TRAINFSTRU", "TRAIN FIRE AND STRUCTURE",
      "TRAINFVEH",  "TRAIN FIRE AND VEHICLE",
      "TRANSF",     "TRANSFORMER FIRE",
      "TREE",       "TREE DOWN",
      "VEHF",       "VEHICLE FIRE",
      "VEHFCOMM",   "VEHICLE FIRE COMMERCIAL",
      "VEHFHM",     "VEHICLE FIRE WITH HAZARDOUS MA",
      "VEHFLDWATR", "VEHICLE IN FLOOD WATER",
      "VEHFPD",     "VEHICLE FIRE PARKING DECK",
      "VEHFSTRUC",  "VEHICLE FIRE THREATENING A STR",
      "VEHFSTRUHM", "VEHICLE FIRE STRUCTURE HAZMAT",
      "VEHFTRAP",   "VEHICLE FIRE SUBJECT TRAPPED",
      "VEHFTRAPHM", "VEHICLE FIRE SUBJECT TRAPPED W",
      "WATER",      "WATER RESCUE",
      "WATERDIS",   "WATERCRAFT IN DISTRESS",
      "WATERWC",    "WATERCRAFT WELFARE CHECK",
      "WILDFIRE",   "WILDLAND FIRE",
      "WIRESDOWN",  "ELECTRICAL WIRES DOWN",
      "WIRESINJ",   "ELECTRICAL WIRES SING INJ",
      "WIRESINJS",  "ELECTRICAL WIRES MULT INJS"
  );

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AN",     "ANGIER",
      "AP",     "APEX",
      "CA",     "CARY",
      "FV",     "FUQUAY-VARINA",
      "GR",     "GARNER",
      "HS",     "HOLLY SPRINGS",
      "KD",     "KNIGHTDALE",
      "MV",     "MORRISVILLE",
      "RA",     "RALEIGH",
      "RDU",    "RDU",
      "RO",     "ROLESVILLE",
      "ST",     "ST",
      "WD",     "WENDELL",
      "WF",     "WAKE FOREST",
      "ZB",     "ZEBULON",

      "WCAN",   "ANGIER",
      "WCAP",   "APEX",
      "WCCA",   "CARY",
      "WCCL",   "CLAYTON",
      "WCCR",   "CREEDMOOR",
      "WCDU",   "DURHAM",
      "WCFV",   "FUQUAY-VARINA",
      "WCGR",   "GARNER",
      "WCHS",   "HOLLY SPRINGS",
      "WCKD",   "KNIGHTDALE",
      "WCMV",   "MORRISVILLE",
      "WCNH",   "NEW HILL",
      "WCRA",   "WAKE COUNTY", // "RALEIGH",
      "WCRO",   "ROLESVILLE",
      "WCWD",   "WENDELL",
      "WCWF",   "WAKE FOREST",
      "WCWS",   "WILLOW SPRINGS",
      "WCYV",   "YOUNGSVILLE",
      "WCZB",   "ZEBULON",

      "CC",     "CHATHAM COUNTY",
      "DC",     "DURHAM COUNTY",
      "FC",     "FRANKLIN COUNTY",
      "GC",     "GRANVILLE COUNTY",
      "JC",     "JOHNSTON COUNTY",
      "NC",     "NASH COUNTY"


  });

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "RDU",     "MORRISVILLE",
      "ST",      "NORTH CAROLINA STATE UNIVERSITY"
  });
}
