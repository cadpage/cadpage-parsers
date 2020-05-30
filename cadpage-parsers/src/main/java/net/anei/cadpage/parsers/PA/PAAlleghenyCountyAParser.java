package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Allegheny County, PA
 */
public class PAAlleghenyCountyAParser extends FieldProgramParser {
  
  public PAAlleghenyCountyAParser() {
    super(CITY_CODES, "ALLEGHENY COUNTY", "PA",
           "CODE PRI CALL CALL+? ( GPS1 GPS2! XINFO+? SRC | ADDR/Z CITY/Y! ( DUP_ADDR CITY | ) ( AT SKIP | ) XINFO+? SRC | PLACE AT CITY? XINFO+? SRC | SRC ) BOX? ID/L+? INFO+ Units:UNIT UNIT+");
    setupCities(EXTRA_CITY_LIST);
    setupGpsLookupTable(PAAlleghenyCountyParser.GPS_TABLE_LOOKUP);
  }
  
  @Override
  public String getFilter() {
    return "@AlleghenyCounty.us,@ACESCAD.comcastbiz.net,messaging@iamresponding.com,777,9300,4127802418";
  }

  private static final Pattern ARCH_ST_EXT = Pattern.compile("\\b(ARCH ST) EXT\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern BUTLER_STREET_EXT = Pattern.compile("\\b(BUTLER ST(?:REET)?) EXT\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String sAddress) {
    sAddress = ARCH_ST_EXT.matcher(sAddress).replaceAll("$1");
    sAddress = BUTLER_STREET_EXT.matcher(sAddress).replaceAll("$1");
    return super.adjustMapAddress(sAddress);
  }
  
  private static final Pattern MARKER = Pattern.compile("ALLEGHENY COUNTY 911:? :|:");
  private static final Pattern TRAILER_PTN = Pattern.compile(" +- +From \\d+ (\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)\\b.*$");
  private static final Pattern MOVE_UP_PTN = Pattern.compile("MOVE-UP: +([A-Z0-9]+) +to +([A-Z0-9]+)\\.?");
  private static final Pattern MOVE_UP_UNIT_PTN = Pattern.compile("\\b([A-Z0-9]+) +to +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Undo some IAR edits :(
    if (subject.equals("Station 125")) body = body.replace('\n', ',');
    
    // There are a number of different message markers
    do {
      Matcher match = MARKER.matcher(body);
      if (match.lookingAt()) {
        body = body.substring(match.end()).trim();
        break;
      }
      
      if (body.startsWith("MOVE-UP")) break;
      
      if (subject.endsWith(" Station")) break;
        
      // And sometimes there is no signature whatsoever
      
    } while (false);
    
    // Drop anything beyond a line break
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    // Remove trailing stuff that we aren't interested in
    Matcher match = TRAILER_PTN.matcher(body);
    if (match.find()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      body = body.substring(0,match.start()).trim();
    }
    
    // If it isn't there or is truncated, remove what we 
    // do find and set the possibly incomplete data flag
    else {
      data.expectMore = true;
      pt = body.indexOf(" - From");
      if (pt >= 0) body = body.substring(0, pt).trim();
    }
    
    // MOVE-UP is a special case
    match = MOVE_UP_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL UNIT DATE TIME");
      data.strCall = "MOVE-UP to " + match.group(2);
      data.strUnit = match.group(1);
      return true;
    }
    
    // MOVE-UPS is an even specialer case
    if (body.startsWith("MOVE-UPS:")) {
      setFieldList("CALL UNIT DATE TIME");
      data.strCall = body;
      match = MOVE_UP_UNIT_PTN.matcher(body);
      while (match.find()) {
        data.strUnit = append(data.strUnit, " ", match.group(1));
      }
      return true;
    }
    
    body = body.replace(" Unit:", " Units:");
    if (!parseFields(body.split(","), 5, data)) return false;
    if (data.strPlace.startsWith("GAP - GREAT ALLEGHENY PASSAGE - ")) {
      data.strAddress = data.strPlace;
      data.strPlace = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new CodeField("[A-Z0-9]+", true);
    if (name.equals("PRI")) return new PriorityField("[A-Z]\\d|\\d[A-Z]", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("GPS1")) return new GPSField(1, "[-+]\\d+\\.\\d+");
    if (name.equals("GPS2")) return new GPSField(2, "[-+]\\d+\\.\\d+");
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("DUP_ADDR")) return new MyDupAddressField();
    if (name.equals("AT")) return new MyAtField();
    if (name.equals("XINFO")) return new MyCrossInfoField();
    if (name.equals("SRC")) return new SourceField("[A-Z]{3}\\d");
    if (name.equals("BOX")) return new BoxField("\\d{5,6}(?: \\d{5,6})*|[A-Z]\\d{5}|[A-Z]{2,3}\\d{2,3}|\\d{3}[A-Z]\\d{2}|[EF]?\\d{3}-[A-Z0-9]{2,3}", true);
    if (name.equals("ID")) return new IdField("[A-Z]{1,5}\\d{9}", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private class MyCallField extends  CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, ", ", field);
    }
  }
  
  // Stock address field is fine, but we want test generator to know
  // that this might be a place field
  private class MyAddressField extends AddressField {
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.endsWith(" TOWN")) field = field.substring(0,field.length()-4) + "TWP";
      if (super.checkParse(field, data)) return true;
      if (field.startsWith("<") && field.endsWith(">")) return true;
      return false;
    }
  }
  
  private class MyDupAddressField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals(getRelativeField(-2));
    }
  }
  
  // AT field is option and hold the real address
  // if present, the previous address turns into a place name
  private class MyAtField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override public boolean checkParse(String field, Data data) {
      if (!field.startsWith("at ")) return false;
      data.strPlace = data.strAddress;
      data.strAddress = "";
      super.parse(field.substring(3).trim(), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern COVID_PTN = Pattern.compile("COVID (?:NEG(?:ATIVE)?|POS(?:ITIVE)?)|(?:NEG(?:ATIVE)?|POS(?:ITIVE)?) COVID");
  private static final Pattern PHONE_PTN = Pattern.compile("\\d{10}");
  private class MyCrossInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = COVID_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strAlert = match.group();
        field = field.substring(match.end()).trim();
      }
      if (field.startsWith("btwn ")) {
        data.strCross = field.substring(5).trim();
      } else if (PHONE_PTN.matcher(field).matches()) {
        data.strPhone = field;
      } else if (field.startsWith("<") && field.endsWith(">")) {
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ALERT X PHONE INFO";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Medical ProQA")) return;
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
  
  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "ALE", "ALEPPO",
      "AS",  "ASPINWALL",
      "ASP", "ASPINWALL",
      "AVA", "AVALON",
      "BAH", "BEN AVON HEIGHTS",
      "BAS", "BELL ACRES",
      "BAV", "BEN AVON",
      "BEL", "BELLEVUE",
      "BKR", "BRACKENRIDGE",
      "BLA", "BLAWNOX",
      "BPK", "BETHEL PARK",
      "BRD", "BRADDOCK",
      "BRE", "BRENTWOOD",
      "BRG", "BRIDGEVILLE",
      "BRH", "BRADDOCK HILLS",
      "BWB", "BALDWIN",
      "BWS", "BRADFORD WOODS",
      "BWT", "BALDWIN TOWNSHIP",
      "CAR", "CARNEGIE",
      "CHA", "CHALFANT",
      "CHE", "CHESWICK",
      "CHU", "CHURCHILL",
      "CLA", "CLAIRTON",
      "COL", "COLLIER",
      "COR", "CORAOPOLIS",
      "CRA", "CRAFTON",
      "CRE", "CRESCENT",
      "CSH", "CASTLE SHANNON",
      "DOR", "DORMONT",
      "DRA", "DRAVOSBURG",
      "DUQ", "DUQUESNE",
      "EDG", "EDGEWORTH",
      "EDR", "EAST DEER",
      "ELB", "ELIZABETH",
      "ELT", "ELIZABETH TWP",
      "EMC", "EAST MCKEESPORT",
      "EMS", "EMSWORTH",
      "EPG", "EAST PITTSBURGH",
      "ETN", "ETNA",
      "EWD", "EDGEWOOD",
      "FIN", "FINDLAY",
      "FOR", "FOREST HILLS ",
      "FOX", "FOX CHAPEL",
      "FPB", "FRANKLIN PARK",
      "FRZ", "FRAZER",
      "FWD", "FORWARD TWP",
      "FWN", "FAWN",
      "GLA", "GLASSPORT",
      "GLE", "GLENFIELD",
      "GOB", "GLEN OSBORNE",
      "GTR", "GREEN TREE",
      "HAM", "HAMPTON",
      "HAR", "HARMAR",
      "HAY", "HAYESVILLE",
      "HEI", "HEIDELBERG",
      "HOM", "HOMESTEAD",
      "HSN", "HARRISON",
      "IND", "INDIANA",
      "ING", "INGRAM",
      "JEF", "JEFFERSON",
      "KEN", "KENNEDY",
      "KIL", "KILBUCK",
      "LEE", "LEET",
      "LIB", "LIBERTY ",
      "LIN", "LINCOLN",
      "LTD", "LEETSDALE",
      "MAR", "MARSHALL",
      "MCC", "MCCANDLESS",
      "MCD", "MCDONALD ",
      "MCK", "MCKEESPORT",
      "MIL", "MILLVALE",
      "MON", "MONROEVILLE",
      "MOO", "MOON",
      "MTL", "MT. LEBANON",
      "MTO", "MT OLIVER",
      "MUN", "MUNHALL ",
      "NBR", "NORTH BRADDOCK",
      "NEV", "NEVILLE ",
      "NFT", "NORTH FAYETTE",
      "NVT", "NORTH VERSAILLES",
      "OAK", "OAKMONT",
      "OHA", "O'HARA",
      "OHI", "OHIO",
      "OKD", "OAKDALE",
      "PEN", "PENN HILLS",
      "PGH", "PITTSBURGH",
      "PIN", "PINE",
      "PIT", "PITCAIRN",
      "PLE", "PLEASANT HILLS",
      "PLU", "PLUM",
      "PVG", "PENNSBURY VILLAGE",
      "PVU", "PORT VUE",
      "RAN", "RANKIN ",
      "RCH", "RICHLAND",
      "RES", "RESERVE ",
      "RKS", "MCKEES ROCKS",
      "ROB", "ROBINSON",
      "ROF", "ROSSLYN FARMS",
      "ROS", "ROSS",
      "SCT", "SCOTT",
      "SEW", "SEWICKLEY",
      "SFT", "SOUTH FAYETTE",
      "SHA", "SHALER",
      "SHI", "SEWICKLEY HILLS",
      "SHP", "SHARPSBURG",
      "SHT", "SEWICKLEY HEIGHTS",
      "SPB", "SPRINGDALE",
      "SPK", "SOUTH PARK ",
      "SPT", "SPRINGDALE TWP.",
      "STO", "STOWE",
      "SVS", "SOUTH VERSAILLES",
      "SWS", "SWISSVALE",
      "TAR", "TARENTUM",
      "TCV", "TURTLE CREEK",
      "THO", "THORNBURG",
      "TRA", "TRAFFORD",
      "USC", "UPPER ST. CLAIR",
      "VER", "VERONA",
      "VSB", "VERSAILLES",
      "WAL", "WALL",
      "WBG", "WILKINSBURG",
      "WDR", "WEST DEER",
      "WEL", "WEST ELIZABETH",
      "WES", "WEST MIFFLIN",
      "WHI", "WHITEHALL",
      "WHM", "WEST HOMESTEAD",
      "WIL", "WILKINS",
      "WIM", "WILMERDING",
      "WOA", "WHITE OAK",
      "WTK", "WHITAKER",
      "WVW", "WEST VIEW",
  });
  
  private static final String[] EXTRA_CITY_LIST = new String[]{
    
    // Armstrong County
    "FREEPORT",
    
    // Butler County
    "ADAMS TWP",
    "MIDDLESEX TWP"
  };
}
