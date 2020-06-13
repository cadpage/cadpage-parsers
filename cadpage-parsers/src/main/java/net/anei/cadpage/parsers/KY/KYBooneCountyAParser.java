package net.anei.cadpage.parsers.KY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;



public class KYBooneCountyAParser extends FieldProgramParser {
  
  public KYBooneCountyAParser() {
    this("BOONE COUNTY", "KY");
  }
  
  public KYBooneCountyAParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState,
          "Location:ADDR/S? EID:ID! TYPE_CODE:CALL! TIME:TIME! Comments:INFO Event_Number:SKIP% Event_Type:SKIP%");
    setupCityValues(CITY_CODES);
  }
  
  @Override
  public String getAliasCode() {
    return "KYBooneCountyA";
  }
  
  @Override
  public String getFilter() {
    return "jfussinger@bcpscc.org,bcpsccky@gmail.com";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
    };
  }
  
  private static final String MARKER = "IPS I/Page Notification";

  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals(MARKER)) break;
      
      if (body.startsWith(MARKER)) {
        body = body.substring(MARKER.length()).trim();
        break;
      }
      
      return false;
    } while (false);
    
    if (!super.parseMsg(body,  data)) return false;
    if (data.strAddress.length() == 0) {
      data.strAddress = data.strCity;
      data.strCity = "";
    }
    return true;
  }
  
  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_EMPTY_ADDR_OK;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_GPS_PTN = Pattern.compile("XY\\((\\d+)(\\d{6}),(\\d+)(\\d{6})\\)[ :@]*(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // Turn GPS coordinates into something we no how to handle
      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.matches()) {
        data.strAddress = "LAT:" + match.group(3) + '.' + match.group(4) + " LON:" + match.group(1) + '.' + match.group(2);
        data.strPlace = match.group(5);
        return;
      }
      
      
      // The match really can not fail
      Parser p = new Parser(field);
      data.strPlace = p.getLastOptional(": @");
      String apt = p.getLastOptional(":APT");
      if (apt.length() == 0) apt = p.getLastOptional(',');
      if (apt.length() == 0) apt = p.getLastOptional(':');
      if (data.strPlace.length() == 0) data.strPlace = p.getLastOptional(":");
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY PLACE APT";
    }
  }
  
  private static final Pattern INFO_GPS_PTN = Pattern.compile(".*? ([-+]?\\d{2,3}\\.\\d{6}) ([-+]?\\d{2,3}\\.\\d{6})\\b *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("SPECIAL ADDRESS COMMENT:")) field = field.substring(27).trim();
      
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.lookingAt()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS INFO";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BCFL", "FLORENCE",
      "BCUN", "UNION",
      "BCWA", "WALTON",
      "BURL", "BURLINGTON",
      "CRIT", "CRITTENDEN",
      "ERLA", "ERLANGER",
      "FLOR", "FLORENCE",
      "FLUN", "FLORENCE UNION",
      "GAC",  "GALATIN",
      "GRC",  "GRANT",
      "HEBR", "HEBRON",
      "INDE", "INDEPENDENCE",
      "KEC",  "KENTON",
      "PETE", "PETERSBURG",
      "UNIO", "UNION",
      "VERO", "VERONA",
      "WALT", "WALTON"
  }); 
}
