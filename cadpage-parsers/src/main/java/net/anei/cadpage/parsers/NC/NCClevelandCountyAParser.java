package net.anei.cadpage.parsers.NC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCClevelandCountyAParser extends DispatchOSSIParser {
  
  private static final Pattern BRACKET_PTN = Pattern.compile(";([^;]*?)\\["); 
  private String bracketTrigger = null;
  private boolean bracket = false;
  
  public NCClevelandCountyAParser() {
    super("CLEVELAND COUNTY", "NC",
           "( NAME PHONE CALL | NAME NAME PHONE CALL | PHONE CALL | NAME NAME CALL | NAME CALL | CALL ) ADDR! ADDR2? ( X X? | PLACE X  X? | PLACE PLACE X X? | ) INFO+");
    setupMultiWordStreets("SANDY RUN CHURCH");
  }
  
  @Override
  public String getFilter() {
    return "CAD@clevelandcounty.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Text Message")) return false;
    body = fixBody(body);
    bracket = false;
    bracketTrigger = null;
    Matcher match = BRACKET_PTN.matcher(body);
    if (match.find()) bracketTrigger = match.group(1).trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("PHONE")) return new PhoneField("\\d{10}");
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]{1,2}[FP]D|\\d{3,4}|[A-Z]+\\d+");
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      
      // We call it the name field, but it can be  many different things
      // Like a county name
      String county = COUNTY_CODES.getProperty(field.toUpperCase());
      if (county != null) {
        data.strCity = county;
        return;
      }
      
      // Possibly a unit field
      if (UNIT_PTN.matcher(field).matches()) {
        data.strUnit = append(data.strUnit, " ", field);
        return;
      }
      
      // Otherwise call it a name
      data.strName = append(data.strName, "-", cleanWirelessCarrier(field));
    }
    
    @Override
    public String getFieldNames() {
      return "NAME UNIT CITY";
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!isValidCall(field)) return false;
      super.parse(field, data);
      return true;
    }
  }
  
  private class MyAddress2Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!NUMERIC.matcher(data.strAddress).matches()) return false;
      String delim = NUMERIC.matcher(field).lookingAt() ? "-" : " ";
      field = append(data.strAddress, delim, field);
      data.strAddress = "";
      super.parse(field, data);
      return true;
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(data.strPlace, "-", field);
    }
  }
  
  private static final Pattern NOT_CROSS_PTN = Pattern.compile("[a-z]");
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (NOT_CROSS_PTN.matcher(field).find()) return false;
      if (field.equals("CHARLESTON PLACE")) return false;
      return super.checkParse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String delim = bracket ? " / " : "-";
      data.strSupp = append(data.strSupp, delim, field);
      if (bracketTrigger != null) {
        bracket |= bracket || field.equals(bracketTrigger);
      }
    }
  }

  /**
   * Convert field separators from dashes to semicolons that the base
   * class expects.  But check each one to make sure it is not part of
   * a recognized hyphenated word, if it is, leave it alone
   * @param body test message to be converted
   * @return converted text message
   */
  private String fixBody(String body) {
    StringBuilder sb = new StringBuilder(body);
    int st = 0;
    for (int pt = 0; pt < sb.length(); pt++) {
      char chr = sb.charAt(pt);
      if (chr == '[') break;
      if (chr == ' ' || chr == ':') st = pt+1;
      else if (chr == '-') {
        String prevWord = sb.substring(st,pt);
        String postWord = HYPHENATED_WORD_MAP.get(prevWord);
        if (postWord == null || !sb.substring(pt+1).startsWith(postWord)) {
          sb.setCharAt(pt, ';');
          st = pt+1;
        }
      }
    }
    return sb.toString().replace(";;", ";");
  }
  
  private static final Map<String,String> HYPHENATED_WORD_MAP = new HashMap<String,String>();
  static {
    for (String word : new String[]{
        "10-50",
        "BELWOOD-LAWNDALE",
        "CASAR-LAWNDALE",
        "FALLSTON-WACO",
        "TASTE-T-DRIVE",
        "T-Mobile"
        }) {
      for (int pt = 0; pt<word.length(); pt++) {
        if (word.charAt(pt) == '-') {
          HYPHENATED_WORD_MAP.put(word.substring(0,pt), word.substring(pt+1));
        }
      }
    }
  }
  
  private static Properties COUNTY_CODES = buildCodeTable(new String[]{
      "LINCOLN COUNTY",    "LINCOLN COUNTY",
      "LINC COUNTY",       "LINCOLN COUNTY",
      "LINC CO",           "LINCOLN COUNTY",
      "LINCOLN CO",        "LINCOLN COUNTY"
  });
  
  static boolean isValidCall(String call) {
    return CALL_SET.contains(call);
  }
  
  private static final Set<String> CALL_SET = new HashSet<String>(Arrays.asList(new String[]{
      "10-50 PI",
      "50PD",
      "A B PAIN",
      "ALREACT",
      "AN/BITE",
      "ASSAULT",
      "ASSIST O",
      "AST/EMS",
      "BACK PAI",
      "C/R/ARRE",
      "CHEST PA",
      "CHOKING",
      "DEATH",
      "DIABETIC",
      "DIF BREA",
      "EM/TRANS",
      "FALL",
      "FIRE ALA",
      "FIRE PD",
      "FIRE/1ST",
      "FIRE/CB",
      "FIRE/CHM",
      "FIRE/COM",
      "FIRE/HAZ",
      "FIRE/HOU",
      "FIRE/INV",
      "FIRE/MH",
      "FIRE/MIS",
      "FIRE/OB",
      "FIRE/PA",
      "FIRE/TRA",
      "FIRE/VEH",
      "FIRE/WG",
      "FIREAPT",
      "HEADACHE",
      "HEART PR",
      "HEAT/COL",
      "HEM/LAC",
      "OT MED",
      "OT25",
      "OVERDOSE",
      "PREGNANC",
      "PSYC PBM",
      "R/TRANS",
      "SEIZURES",
      "SICKNESS",
      "STAB/SHT",
      "STEMI",
      "STROKE",
      "TRAUMA",
      "UNCONSCI",
      "UNDER CONTROL",
      "UNK MED"
  }));
}
