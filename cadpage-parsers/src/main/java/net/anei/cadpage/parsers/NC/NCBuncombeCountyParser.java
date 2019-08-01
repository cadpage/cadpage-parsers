package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Buncombe county, NC
 */
public class NCBuncombeCountyParser extends DispatchOSSIParser {
  
  public NCBuncombeCountyParser() {
    super(CITY_LIST, "BUNCOMBE COUNTY", "NC",
          "FYI? ( CANCEL ADDR UNIT PHONE END " +
               "| CALL? ADDR! UNIT1? UNIT2? SPEC/N+ )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@buncombecounty.org";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("S:") && body.endsWith(" M:")) {
      body = body.substring(2, body.length()-3).trim();
    }
    body = body.replaceAll("\n", " ");
    
    // Sanity check - must have at least one semicolons
    if (!body.contains(";")) return false;
    
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    
    if (! super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) data.strCall = "Unknown";
    if (data.strCity.endsWith(" EOC")) {
      data.strCity = data.strCity.substring(0, data.strCity.length()-4) + " COUNTY";
    }
    return true;
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("PREFIX")) return new CallField("UNDER CONTROL|Working fire", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT1")) return new UnitField("[A-Z]{1,5}\\d+|", true);
    if (name.equals("UNIT2")) return new MyUnitField("[A-Z]{1,5}[0-9]{1,4}[A-Z]?(?:,.*)?|", true);
    if (name.equals("SPEC")) return new SpecField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return false;
      if (Character.isDigit(field.charAt(0))) return false;
      if (!CALL_PATTERN.matcher(field).find()) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    
    public MyUnitField(String pattern, boolean hard) {
      super(pattern, hard);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (data.strUnit.length() > 0) {
        if (Pattern.compile("\\b" + data.strUnit + "\\b").matcher(field).find()) data.strUnit = "";
      }
      data.strUnit = append(data.strUnit, ",", field);
    }
  }
  
  /**
   * Covers fields entered in random order between the Unit and cross street
   */

  private static final Pattern CALL_PFX_PTN = Pattern.compile("\\((?:FR|Z)\\) *(.*)");
  private static final Pattern CALL_PATTERN = 
    Pattern.compile("\\b(?:ALARM|ALLERGIES|ANIMAL BITES|ASSAULT|ASSIST|ASST|ASSISTANCE|BLOCKING|BURNS?|CARDIAC|CHECK|CHECKED P/T|CHOKING|CONVULSIONS|DAMAGE|DETECTOR|DRONE|EMS|EXPOSURE|FALL|FALLS|FILL CASCADE|FIRE(?! DEPT| DEPARTMENT)|GAS|HEART|HELP|HEMORRHAGE|HIT|INJURY|INJURIES|LINES? DOWN|LOCKED IN|LOCKED OUT|MAN DOWN|MULTIPLE|MUTUAL AID|MVA|NO TONES|ODER|ODOR|OVERDOSE|OUT OF CONTROL|PAIN|PAINS|POISONING|POWER LINES?|PROBLEM|PROBLEMS|PUBLIC SERVICE|PULL STATION|REMOVAL|RESCUE|ROADWAY|SEARCH|SEIZURES|SHOCK|SICK|SMELL|SMOKE|SMOKING|STAND ?BY|STRIKE|STROKE|STUCK|SMOKING|SUICIDE|TEST CALL|TRANSFER|TRANSPORT|TREE DOWN|UNCONSCIOUS|WATER LINE|WORKING|WRECK)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern PHONE_DIST_PTN = Pattern.compile("(\\d{10}) DIST.*");
  private class SpecField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      
      field = stripFieldEnd(field, ",");
      
      String upField = field.toUpperCase();
      if (upField.startsWith("VERIZON")) return;
      if (upField.toUpperCase().startsWith("T-MOBILE")) return;
      if (upField.startsWith("SPRINT")) return;
      if (upField.startsWith("US CELLULAR")) return;
      if (upField.startsWith("AT&T MOBILITY")) return;
      
      String nextFld = getRelativeField(+1);
      if (nextFld.startsWith("Radio Channel:")) {
        nextFld = nextFld.substring(14).trim();
        if (field.equals(nextFld)) return;
      }
      
      Matcher match;
      if (field.startsWith("Radio Channel:")) {
        data.strChannel = field.substring(14).trim();
      }
      
      else if (data.strCity.length() == 0 && isCity(field)) {
        data.strCity = field;
      }
      
      else if (data.strName.length() == 0 && field.length() <= 35 && field.contains(",")) {
        data.strName = field;
      }
      
      else if (data.strPhone.length() == 0 && field.length() == 10 && NUMERIC.matcher(field).matches() && 
              !field.startsWith("1") && ! field.contains("00")) {
        data.strPhone = field;
      }
      
      else if (data.strCallId.length() == 0 && field.length() >= 8 && NUMERIC.matcher(field).matches()) { 
        data.strCallId = field;
      }
      
      else if ((match = PHONE_DIST_PTN.matcher(field)).matches()) {
        data.strPhone = match.group(1);
      }
      
      else if (!field.startsWith("FM") && checkAddress(field) == STATUS_STREET_NAME) {
        data.strCross = append(data.strCross, " & ", field);
      }
      
      else if ((match = CALL_PFX_PTN.matcher(field)).matches()) {
        data.strCall = append(match.group(1), " - ", data.strCall);
      }
      
      else if (data.strCall.length() == 0 && CALL_PATTERN.matcher(field).find()) {
        data.strCall = field;
      }
      
      else if (!upField.contains("DIST:")) {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CITY CALL NAME PHONE INFO X ID CH";
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "ASHEVILLE",

      // Towns
      "BILTMORE FOREST",
      "BLACK MOUNTAIN",
      "MONTREAT",
      "WEAVERVILLE",
      "WOODFIN",

      // Townships
      "ASHEVILLE",
      "AVERY CREEK",
      "BLACK MOUNTAIN",
      "BROAD RIVER",
      "FAIRVIEW",
      "FLAT CREEK",
      "FRENCH BROAD",
      "HAZEL [18]",
      "IVY",
      "LEICESTER",
      "LIMESTONE",
      "LOWER HOMINY",
      "REEMS CREEK",
      "SANDY MUSH",
      "SWANNANOA",
      "WOODFIN",
      "UPPER HOMINY",

      // Census-designated places
      "AVERY CREEK",
      "BENT CREEK",
      "FAIRVIEW",
      "ROYAL PINES",
      "SWANNANOA",

      // Unincorporated communities
      "ALEXANDER",
      "ARDEN",
      "BARNARDSVILLE",
      "CANDLER",
      "ENKA",
      "FLAT CREEK",
      "FORKS OF IVY",
      "JUPITER",
      "LEICESTER",
      "RIDGECREST",
      "SKYLAND",
      "STOCKSVILLE",
      
      // Haywood County
      "HAYWOOD COUNTY",
      "HAYWOOD CO",
      "HAYWOOD",
      "BEAVERDAM",
      "CANTON",
      "EAST FORK",
      "PIGEON",
      
      // Henderson County
      "HENDERSON COUNTY",
      "HENDERSON CO",
      "HENDERSON",
      "BALFOUR",
      "CLEAR CREEK",
      "EDNEYVILLE",
      "FLETCHER",
      "GERTON",
      "HENDERSONVILLE",
      "HOOPERS CREEK",
      "MILLS RIVER",
      "MOUNTAIN HOME",
      
      // Madison County
      "MADISON COUNTY",
      "MADISON CO",
      "MADISON EOC",
      "MADISON",
      "BEECH GLENN",
      "MARS HILL",
      "MARSHALL",
      "NORTH MARSHALL",
      "SANDY MUSH",
      "SOUTH MARSHALL",
      "SPRING CREEK",
      
      // McDowell County
      "MCDOWELL COUNTY",
      "MCDOWELL CO",
      "MCDOWELL",
      "COLD FORT",
      "CROOKED CREEK",
      "OLD FORT",
      
      // Rutherford County
      "RUTHERFORD COUNTY",
      "RUTHERFORD CO",
      "RUTHERFORD",
      "CHIMNEY ROCK",
      "LAKE LURE",
      
      // Yancy Couty
      "YANCY COUNTY",
      "YANCY CO",
      "YANCY",
      "PENSACOLA",
      "SOUTH TOE"
  };
}
