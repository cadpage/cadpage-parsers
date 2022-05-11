package net.anei.cadpage.parsers.MD;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MDGarrettCountyParser extends FieldProgramParser {
  
  public MDGarrettCountyParser() {
    super(CITY_LIST, "GARRETT COUNTY", "MD", 
          "ADDR/S6 CITY? ( X | X1 X2 | ) CALL CALL2/SDS+? INFO/SDS+? UNIT_TIME! END");
  }
  
  @Override
  public String getFilter() {
    return "garrettco911@garrettcounty.org,garrettcounty911@gc911.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    String[] flds = body.replace(" - - ", " - ").split(" - ");
    if (flds.length < 3) return false;
    String prefix = null;
    int pt = flds[0].indexOf('\n');
    if (pt >= 0) { 
      prefix = flds[0].substring(0,pt).trim();
      flds[0] = flds[0].substring(pt+1).trim();
    }
    if (!parseFields(flds, data)) return false;
    if (prefix != null) {
      data.msgType = MsgType.RUN_REPORT;
      data.strCall = append(prefix, " - ", data.strCall);
    }
    
    String city = data.strCity;
    if (city.length() > 0) {
      city = stripFieldEnd(city, " TWP");
      city = stripFieldEnd(city, " TOWNSHIP");
      if (PA_CITY_SET.contains(city)) {
        data.strState = "PA";
      } else if (WV_CITY_SET.contains(city)) {
        data.strState = "WV";
      }
    }
    
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField("\\[ *(.*?) *\\]", true);
    if (name.equals("X1")) return new MyCrossField("\\[ *(.*?)", true);
    if (name.equals("X2")) return new MyCrossField("(.*?) *\\]", true);
    if (name.equals("CALL2")) return new CallField("HOT|COLD|[AB]LS", true);
    if (name.equals("UNIT_TIME")) return new MyUnitTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_MM_PTN = Pattern.compile("(.* \\d*MM\\d*(?: [NSEW])?\\b) *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      while (field.startsWith("*")) field = field.substring(1).trim();
      Parser p = new Parser(field);
      String apt = p.getLastOptional('#');
      String place = p.getLastOptional(',');
      String addr = p.get();
      if (addr.endsWith(" COUNTY")) {
        data.strCity = addr;
        Matcher match = ADDR_MM_PTN.matcher(place);
        if  (match.matches()) {
          parseAddress(match.group(1).trim(), data);
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, match.group(2), data);
        } else {
          parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT, place, data);
        }
        data.strPlace = append(data.strPlace, " - ", getLeft());
      } else {
        super.parse(addr, data);
        data.strPlace = place;
      }
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST PLACE";
    }
  }
  
  private class MyCrossField extends CrossField {
    
    public MyCrossField(String pattern, boolean required) {
      super(pattern, required);
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return false;
    }
  }

  private static final Pattern UNIT_TIME_PTN = Pattern.compile("(?:([ A-Z0-9]+?) )??(?:(\\d{7}) )?(\\d\\d:\\d\\d)(?: +(.*))?");
  private class MyUnitTimeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strUnit = getOptGroup(match.group(1));
      data.strCallId = getOptGroup(match.group(2));
      data.strTime = match.group(3);
      data.strCall = append(data.strCall, " - ", getOptGroup(match.group(4)));
    }

    @Override
    public String getFieldNames() {
      return "UNIT ID TIME";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("TG:")) {
        data.strChannel = append(data.strChannel, "/", field.substring(3).trim());
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CH";
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Towns
    "ACCIDENT",
    "DEER PARK",
    "FRIENDSVILLE",
    "GRANTSVILLE",
    "KITZMILLER",
    "LOCH LYNN HEIGHTS",
    "MOUNTAIN LAKE PARK",
    "OAKLAND",

    // Census-designated places
    "BLOOMINGTON",
    "CRELLIN",
    "FINZEL",
    "GORMAN",
    "HUTTON",
    "JENNINGS",
    "SWANTON",

    // Unincorporated communities
    "ALTAMONT",
    "ASHER GLADE",
    "AVILTON",
    "BETHEL",
    "BEVANSVILLE",
    "BITTINGER",
    "BLOOMING ROSE",
    "CASSELMAN",
    "EAST VINDEX",
    "ELDER HILL",
    "ENGLE MILL",
    "FAIRVIEW",
    "FLOYD",
    "FORT PENDLETON",
    "FOXTOWN",
    "FRICKS CROSSING",
    "FROSTBURG",
    "GORTNER",
    "GRAVEL HILL",
    "GREEN GLADE",
    "HAZELHURST",
    "HERRINGTON MANOR",
    "HI-POINT",
    "HIGH POINT",
    "HOYES",
    "HOYES RUN",
    "KAESE MILL",
    "KEARNEY",
    "KEMPTON",
    
    // Parks
    "DEEP CREEK",
    "MT LAKE PARK",

    // Somerset county, PA
    "ADDISON",
    "BENSON",
    "BERLIN",
    "BOSWELL",
    "CALLIMONT",
    "CASSELMAN",
    "CENTRAL CITY",
    "CONFLUENCE",
    "GARRETT",
    "HOOVERSVILLE",
    "INDIAN LAKE",
    "JENNERSTOWN",
    "MEYERSDALE",
    "NEW BALTIMORE",
    "NEW CENTERVILLE",
    "PAINT",
    "ROCKWOOD",
    "SALISBURY",
    "SEVEN SPRINGS",
    "SHANKSVILLE",
    "SOMERSET",
    "STOYSTOWN",
    "URSINA",
    "WELLERSBURG",
    "WINDBER",

    "ADDISON TWP",
    "ALLEGHENY TWP",
    "BLACK TWP",
    "BROTHERSVALLEY TWP",
    "CONEMAUGH TWP",
    "ELK LICK TWP",
    "FAIRHOPE TWP",
    "GREENVILLE TWP",
    "JEFFERSON TWP",
    "JENNER TWP",
    "LARIMER TWP",
    "LINCOLN TWP",
    "LOWER TURKEYFOOT TWP",
    "MIDDLECREEK TWP",
    "MILFORD TWP",
    "NORTHAMPTON TWP",
    "OGLE TWP",
    "PAINT TWP",
    "QUEMAHONING TWP",
    "SHADE TWP",
    "SOMERSET TWP",
    "SOUTHAMPTON TWP",
    "STONYCREEK TWP",
    "SUMMIT TWP",
    "UPPER TURKEYFOOT TWP",
    
    "ADDISON TOWNSHIP",
    "ALLEGHENY TOWNSHIP",
    "BLACK TOWNSHIP",
    "BROTHERSVALLEY TOWNSHIP",
    "CONEMAUGH TOWNSHIP",
    "ELK LICK TOWNSHIP",
    "FAIRHOPE TOWNSHIP",
    "GREENVILLE TOWNSHIP",
    "JEFFERSON TOWNSHIP",
    "JENNER TOWNSHIP",
    "LARIMER TOWNSHIP",
    "LINCOLN TOWNSHIP",
    "LOWER TURKEYFOOT TOWNSHIP",
    "MIDDLECREEK TOWNSHIP",
    "MILFORD TOWNSHIP",
    "NORTHAMPTON TOWNSHIP",
    "OGLE TOWNSHIP",
    "PAINT TOWNSHIP",
    "QUEMAHONING TOWNSHIP",
    "SHADE TOWNSHIP",
    "SOMERSET TOWNSHIP",
    "SOUTHAMPTON TOWNSHIP",
    "STONYCREEK TOWNSHIP",
    "SUMMIT TOWNSHIP",
    "UPPER TURKEYFOOT TOWNSHIP",
    
    // Grant County
    "BAYARD"

  };
  
  private static final Set<String> PA_CITY_SET = new HashSet<String>(Arrays.asList(
      "ADDISON",
      "BENSON",
      "BERLIN",
      "BOSWELL",
      "CALLIMONT",
      "CASSELMAN",
      "CENTRAL CITY",
      "CONFLUENCE",
      "GARRETT",
      "HOOVERSVILLE",
      "INDIAN LAKE",
      "JENNERSTOWN",
      "MEYERSDALE",
      "NEW BALTIMORE",
      "NEW CENTERVILLE",
      "PAINT",
      "ROCKWOOD",
      "SALISBURY",
      "SEVEN SPRINGS",
      "SHANKSVILLE",
      "SOMERSET",
      "STOYSTOWN",
      "URSINA",
      "WELLERSBURG",
      "WINDBER",

      "ALLEGHENY",
      "BLACK",
      "BROTHERSVALLEY",
      "CONEMAUGH",
      "ELK LICK",
      "FAIRHOPE",
      "FAYETTE",
      "GREENVILLE",
      "JEFFERSON",
      "JENNER",
      "LARIMER",
      "LINCOLN",
      "LOWER TURKEYFOOT",
      "MIDDLECREEK",
      "MILFORD",
      "NORTHAMPTON",
      "OGLE",
      "PAINT",
      "QUEMAHONING",
      "SHADE",
      "SOMERSET",
      "SOUTHAMPTON",
      "STONYCREEK",
      "SUMMIT",
      "UPPER TURKEYFOOT"
  ));
  
  private static final Set<String> WV_CITY_SET = new HashSet<String>(Arrays.asList(
      "BAYARD",
      "GRANT",
      "MINERAL",
      "PRESTON",
      "TUCKER"
  ));
}
