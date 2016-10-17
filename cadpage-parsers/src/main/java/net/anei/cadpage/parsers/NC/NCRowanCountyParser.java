package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCRowanCountyParser extends DispatchOSSIParser {
  
  public NCRowanCountyParser() {
    super(CITY_CODES, "ROWAN COUNTY", "NC",
           "FYI? CALL ADDR! ( CITY | X/Z CITY | X/Z X/Z CITY | ) XPLACE+? ( INFO | MAP_CH_UNIT MAP_CH_UNIT+? ) INFO+");
    setupSpecialStreets("NEW ST");
  }
  
  @Override
  public String getFilter() {
    return "9300,CAD";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    boolean ok = body.startsWith("CAD:");
    if (!ok) body = "CAD:" + body;
    if (!super.parseMsg(body, data)) return false;
    
    // If we didn't have the CAD: prefix and don't have a city, this is just
    // to chancy to accept
    if (!ok && data.strCity.length() == 0) return false;
    
    if (data.strCity.equals("OUT OF COUNTY")) {
      data.defCity = "";
    }

    return data.strAddress.length() > 0;
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equals("OUT OF COUNTY")) return "";
    return city;
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("XPLACE")) return new MyCrossPlaceField();
    if (name.equals("MAP_CH_UNIT")) return new MyMapChannelUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern BAD_CALL_PTN = Pattern.compile("[^ ]/[^ ]+/[^ ]|^[A-Z]\\d+[A-Z]?-");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (BAD_CALL_PTN.matcher(field).find()) abort();
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*)(?: - |~)(.*)");
  private static final Pattern BAD_CITY_PTN = Pattern.compile("[NSEW]|[NS][EW]");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCity = match.group(2).trim();
        
        // Cities that look like directions is a feature of Davidson County alerts
        if (BAD_CITY_PTN.matcher(data.strCity).matches()) abort();
        if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
      }
      if (field.endsWith(" DR DR") || field.endsWith(" RD RD")) {
        field = field.substring(0,field.length()-3);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  // Check for city append with following cross/place field :(
  private static final Pattern CITY_PLACE_PTN = Pattern.compile("([A-Z]{3,4})((?:DIST:|\\(S\\)).*)");
  private class MyCityField extends MyCrossPlaceField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      String city;
      if (field.length() <= 4) {
        city = field;
        field = "";
      } else {
        Matcher match = CITY_PLACE_PTN.matcher(field);
        if (!match.matches()) return false;
        city = match.group(1);
        field = match.group(2);
      }
      city = CITY_CODES.getProperty(city);
      if (city == null) return false;
      data.strCity = city;
      if (field.length() > 0) super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "CITY " + super.getFieldNames();
    }
  }
  
  private static final Pattern CODE_DESC_PTN = Pattern.compile("(\\d{1,3}[A-Z]\\d{1,2}) +(.*)");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|LOT) *(.*)");
  private class MyCrossPlaceField extends MyPlaceField {
    
    @Override
    public void parse(String field, Data data) {
      
      // If this is a DavidsonCountyA call ID, we need to reject it
      // If it looks like a phone number (also 10 digits) accept it
      if (field.length() == 10 && NUMERIC.matcher(field).matches()) { 
        if (field.startsWith("20")) abort();
        data.strPhone = field;
        return;
      }
      
      // This is a catchall field that can contains a lot of things
      // See if it is a call code followed by a description
      Matcher match = CODE_DESC_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strSupp = match.group(2);
        return;
      }
      
      // See if is an apt/lot number
      match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        return;
      }
      
      // See if this looks like a set of cross streets
      if (field.endsWith(" DR DR") || field.endsWith(" RD RD")) {
        field = field.substring(0,field.length()-3);
      }
      if (field.endsWith("CREEK") || field.endsWith("XING") || isValidAddress(field)) {
        data.strCross = append(data.strCross, " / ", field);
        return;
      } 
      
      // Otherwise it is a place field
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE INFO " + super.getFieldNames() + " X PHONE";
    }
  }
  
  private static final Pattern MAP_PTN = Pattern.compile("\\d{3,4}");
  private static final Pattern CHANNEL_PTN = Pattern.compile("OPS.*");
  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]+\\d+[A-Z]?|\\d+[A-Z]+\\d|[A-z0-9]+,[A-Z0-9,]+|[A-Z]{2}|DCC");
  private static final Pattern ID_PTN = Pattern.compile("\\d{8}");
  private class MyMapChannelUnitField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strMap.length() == 0) {
        Matcher match = MAP_PTN.matcher(field);
        if (match.matches()) {
          data.strMap = field;
          return true;
        }
      }
      
      if (data.strChannel.length() == 0) {
        Matcher match = CHANNEL_PTN.matcher(field);
        if (match.matches()) {
          data.strChannel = field;
          return true;
        }
      }
      
      if (data.strUnit.length() == 0) {
        Matcher match = UNIT_PTN.matcher(field);
        if (match.matches()) {
          data.strUnit = field;
          return true;
        }
      }
      
      if (ID_PTN.matcher(field).matches()) {
        data.strCallId = field;
        return true;
      }
      
      if (field.startsWith("**")) {
        data.strSupp = field;
        return true;
      }
      
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "MAP CH UNIT ID INFO";
    }
  }
  
  /**
   * Handles the common place field processinging common to both the MyOptionalPlaceField and
   * MyCrossPlaceField classes
   */
  private static final Pattern PLACE_PTN = Pattern.compile("(.*)\\(S\\)(.*)\\(N\\)(.*)");
  private abstract class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_PTN.matcher(field);
      if (match.matches()) {
        for (int ii = 1; ii<=3; ii++) {
          processPart(match.group(ii).trim(), data);
        }
      }
      
      else {
        processPart(field, data);
      }
    }

    private void processPart(String part, Data data) {
      if (part.length() == 0) return;
      
      boolean apt = false;
      
      Matcher match = APT_PTN.matcher(part);
      if (match.matches()) {
        apt = true;
        part = match.group(1);
      }
      
      else if (data.strCross.length() == 0 && part.length() <= 5 && !part.contains(" ") && !part.equals("MM")) {
        apt = true;
      }
      
      if (apt) {
        if (!part.equals(data.strApt)) data.strApt = append(data.strApt, "-", part);
      }
      else if (!part.equals(data.strPlace)) {
        data.strPlace = append(data.strPlace, " - ", part);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }
  
  private static final Pattern OPT_INFO_PTN = Pattern.compile("(?![A-Z]{0,4}DIST:|\\d{1,3}[A-Z]\\d{1,2} ).*(?:[a-z\\{'`]|\\bHOLD\\b|\\bON THE\\b|\\bCALLER\\b|\\bLOCATED\\b).*");
  private static final Pattern INFO_CHANNEL_PTN = Pattern.compile("Radio Channel: *(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.contains("(S)")) return false;
      if (!field.contains(" ")) return false;
      if (field.length() < 50 && !OPT_INFO_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_CHANNEL_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(1);
        return;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CH";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CHGV", "CHINA GROVE",
      "CLEV", "CLEVELAND",
      "CLVD", "CLEVELAND",
      "COOL", "COOLEEMEE",             
      "ESPN", "EAST SPENCER",
      "FATH", "FAITH",
      "GOLD", "GOLD HILL",
      "GRQY", "GRANITE QUARRY",
      "KAN",  "KANNAPOLIS",
      "KANN", "KANNAPOLIS",
      "LAND", "LANDIS",
      "MOCK", "MOCKSVILLE",
      "MOOR", "MOORESVILLE",
      "MTUL", "MT ULLA",
      "RICH", "RICHFIELD",
      "ROCK", "ROCKWELL",
      "SALS", "SALISBURY",
      "SPEN", "SPENCER",
      "WOOD", "WOODLEAF",
      
      // Cabarrus County, NC
      "CON",  "CONCORD",
      "CONC", "CONCORD",
      
      // Out of County
      "OOC",  "OUT OF COUNTY"
  }); 
}
