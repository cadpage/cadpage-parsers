package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;



public class NCWakeCountyAParser extends FieldProgramParser {
  
  public NCWakeCountyAParser() {
    super(CITY_CODES, "WAKE COUNTY", "NC",
           "Inc:CALL! Map:MAP! Add:ADDR! Loc:PLACE! Apt:APT! CS:X? Unt:UNIT! TG:CH! Cty:CITY! Comm:INFO INFO+");
  }
  
  @Override
  public String getFilter() {
    return "wcps@wakegov.com";
  }

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
  private static final Pattern INFO_ADDR_MARK_PTN = Pattern.compile("\\. : [A-Z]{4}");
  
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
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapCity(String city) {
    city = convertCodes(city.toUpperCase(), MAP_CITY_TABLE);
    return super.adjustMapCity(city);
  }
  
  private static final CodeTable CALL_CODES = new StandardCodeTable(
                  
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
      "FALM2",      "RFD SECOND ALARM",
      "FALM3",      "RFD THIRD ALARM",
      "FALM4",      "RFD FOURTH ALARM",
      "FALM5",      "RFD FIFTH ALARM",
      "FAME",       "MEDICAL ALARM - FIRE",
      "FAR",        "ALLERGIC REACTION",
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
      "FSEIZ",      "SEIZURE",
      "FSHOCK",     "ELECTROCUTION",
      "SFICKF",     "SICK CALL FIRST RESPONDER",
      "FSTAB",      "STABBING",
      "FTEST",      "TEST",
      "FUNCON",     "UNCONSCIOUS/FAINTING",
      "FWALK",      "WALK IN",
      "FWF",        "WORKING FIRE",
      "FWHF",       "WORKING HIGH RISE FIRE",
      "FWHF2",      "HIGH RISE FIRE 2ND ALARM",
      "FWHF3",      "HIGH RISE FIRE 3RD ALARM",
      "FWHF4",      "HIGH RISE FIRE 4TH ALARM",
      "FWHF5",      "HIGH RISE FIRE 5TH ALARM"
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
