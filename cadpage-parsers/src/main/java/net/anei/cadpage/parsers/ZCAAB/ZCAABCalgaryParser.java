package net.anei.cadpage.parsers.ZCAAB;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
/**
 * Calgary, AB, CA
 */
public class ZCAABCalgaryParser extends FieldProgramParser {
  
  public ZCAABCalgaryParser() {
    super(CITY_TABLE, "CALGARY", "AB",
           "Add:ADDR/S4 Map:MAP Det:CALL! FireTAC:CH Evt:ID!");
  }

  @Override
  public String getFilter() {
    return "Fire-EMSCADTeam@calgary.ca";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if(!subject.equals("CAD SMS Event")) return false;
    
    int info = body.indexOf("\n\n\n");
    if(info >= 0) {
      body = body.substring(0, info);
    }
    
    return super.parseMsg(subject, body, data);
  } 
  
  private class MyCallField extends CallField {
    
    @Override
    public void parse(String field, Data data) {
      String desc = STANDARD_CODES.getCodeDescription(field);
      if(desc != null && desc.length() > 0) {
        data.strCall = desc;
        data.strCode = field;
      }
      else {
        data.strCall = field;
      }
      
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CODE";
    }
  }
  
  private static final Pattern ADDR_PLACE_MRK_PTN = Pattern.compile(":(?: @)?");
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(": alias");
      if (pt >= 0) field = field.substring(0,pt).trim();
      Matcher match = ADDR_PLACE_MRK_PTN.matcher(field);
      if (match.find()) {
        String place = field.substring(match.end()).trim();
        field = field.substring(0,match.start()).trim();
        pt = place.indexOf(':');
        if (pt >= 0) {
          data.strSupp = place.substring(pt+1).trim();
          place = place.substring(0,pt).trim();
        }
        data.strPlace = place;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE INFO";
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("#")) field = field.substring(1).trim();
      super.parse(field, data);
    }
  }
 
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final StandardCodeTable STANDARD_CODES = new StandardCodeTable(
      "AC1",      "Airport Level 1 Standby",
      "AC2",      "Airport Level 2 Standby",
      "AC3",      "Aircraft Crash",
      "AHJ",      "Airport Hijacking Incident",
      "AMS",      "Airport Miscellaneous Response",
      "ANIMAL",   "Animal Rescue/Service Call",
      "ANIMALC",  "Animal Rescue/Service Call",
      "APB",      "Aircraft Bomb Threat",
      "ATB",      "Airport Terminal Bomb Threat",
      "BBQ",      "Barbecue Fire",
      "BEL",      "Alarm Bells Ringing",
      "BLD",      "Building Fire",
      "BURN",     "Burning Complaint",
      "CAS",      "Catalogued Alarm",
      "CASC",     "Catalogued Alarm - Cold Response",
      "CASCO",    "Catalogued Carbon Monoxide Alarm",
      "CASGAS",   "Catalogued Gas Alarm",
      "CBRN",     "Chemical/Biological/Radiological/Nuclear Incident",
      "CO",       "Carbon Monoxide Incident - Hot Response",
      "COC",      "Carbon Monoxide Incident - Cold Response",
      "CON",      "Container Fire",
      "CSR",      "Confined Space Rescue",
      "EXP",      "Explosion",
      "FEN",      "Fence Fire",
      "FLARE",    "Flaring Operations",
      "FLO",      "Flooding Incident",
      "FLOC",     "Flooding Incident - Cold Response",
      "GAR",      "Garage Fire",
      "GARHM",    "Garage Fire with Hazardous Materials Present or Involved",
      "GASA",     "Small Gas Leak",
      "GASHM",    "Large Gas Leak or Odor in Area",
      "GASL",     "Gas Line Break",
      "GRA",      "Grass Fire",
      "HAR",      "High Angle Rescue",
      "HAZ",      "Public Hazard",
      "HAZ70",    "Public Hazard on Roadway",
      "HAZMAT",   "Hazardous Materials Incident",
      "HAZMATC",  "Hazardous Materials Incident - Cold Response",
      "HSE",      "House Fire",
      "INV",      "Investigation",
      "INVC",     "Investigation - Cold Response",
      "NFR",      "No Fire Reset Alarms",
      "ODC",      "Odor Complaint",
      "ODCC",     "Odor Complaint - Cold Response",
      "RAL",      "Railway/Railroad Incident",
      "RSC",      "Rescue Non-Specific",
      "RSCC",     "Rescue Non-Specific - Cold Response",
      "RUB",      "Rubbish Fire",
      "SHARPS",   "Sharps Pickup",
      "SHARPSC",  "Sharps Pickup - Cold Response",
      "TEST",     "Test Event",
      "VEH",      "Vehicle Fire",
      "VEHHM",    "Vehicle Fire Involving Hazardous Materials",
      "WRC",      "Water Rescue",
      "WRIC",     "Water Rescue Inv - Cold Response"
  );
  
  private static final Properties CITY_TABLE = buildCodeTable(new String[] {
      "AIRD",  "Airdrie",
      "ALDE",  "Aldersyde",
      "ARRO",  "Arrowwood",
      "BALZ",  "Balzac",
      "BANF",  "Banff",
      "BEIS",  "Beiseker",
      "BENC",  "Benchlands",
      "BIGH",  "Municipal District of Bighorn",
      "BKDM",  "Black Diamond",
      "BLAI",  "Blairmore",
      "BLCK",  "Blackie",
      "BLVU",  "Bellevue",
      "BOTT",  "Bottrel",
      "BRAG",  "Bragg Creek",
      "CALG",  "Calgary",
      "CANM",  "Canmore",
      "CARO",  "Caroline",
      "CARS",  "Carstairs",
      "CAYL",  "Cayley",
      "CHAM",  "Champion",
      "CHEA",  "Cheadle",
      "CHES",  "Chestermere",
      "CLAR",  "Claresholm",
      "CLEA",  "Clearwater County",
      "COCH",  "Cochrane",
      "COCL",  "Cochrane Lake",
      "COLE",  "Coleman",
      "CONR",  "Conrich",
      "CREM",  "Cremona",
      "CROS",  "Crossfield",
      "DALE",  "Dalemead",
      "DEWI",  "DeWinton",
      "DIDS",  "Didsbury",
      "DMNF",  "Dead Man's Flats",
      "EXSH",  "Exshaw",
      "FMCD",  "Fort Macleod",
      "FOOT",  "Municipal District of Foothills",
      "GHOS",  "Ghost Lake Village",
      "GLEI",  "Gleichen",
      "GRNM",  "Granum",
      "HART",  "Hartell",
      "HARV",  "Harvie Heights",
      "HERI",  "Heritage Pointe",
      "HIRV",  "High River",
      "ID09",  "Banff National Park (Improvement District 9)",
      "INDU",  "Indus",
      "IRRI",  "Irricana",
      "JANE",  "Janet",
      "KANA",  "Kananaskis",
      "KATH",  "Kathyrn",
      "KEOM",  "Keoma",
      "KNEE",  "Kneehill County",
      "LAKL",  "Lake Louise",
      "LANG",  "Langdon",
      "LONG",  "Longview",
      "MILO",  "Milo",
      "MLRV",  "Millarville",
      "MNTV",  "Mountain View County",
      "NAKO",  "Stoney Nakoda First Nation",
      "NANT",  "Nanton",
      "OKOT",  "Okotoks",
      "OLDS",  "Olds",
      "PRGR",  "Priddis Greens",
      "PRID",  "Priddis",
      "REDM",  "Redwood Meadows",
      "REDW",  "Redwood Meadows",
      "ROCK",  "Rocky View County",   // Was given to us as "Rockyview County"
      "SHEP",  "Shepard",
      "STAV",  "Stavely",
      "STRA",  "Strathmore",
      "SUND",  "Sundre",
      "TSUU",  "Tsuu T'ina First Nation",
      "TURN",  "Turner Valley",
      "VLCY",  "Vulcan County",
      "VULC",  "Vulcan",
      "WAIP",  "Waiparous Village",
      "WC26",  "Municipal District of Willow Creek",
      "WHEA",  "Wheatland County",
      "WTRV",  "Water Valley"
  });
  
}
