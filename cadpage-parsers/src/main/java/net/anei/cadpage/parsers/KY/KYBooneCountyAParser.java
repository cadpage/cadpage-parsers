package net.anei.cadpage.parsers.KY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class KYBooneCountyAParser extends FieldProgramParser {
  
  private static final String MARKER = "IPS I/Page Notification";
  
  public KYBooneCountyAParser() {
    super(CITY_CODES, "BOONE COUNTY", "KY",
          "Location:ADDR/S? Event_Number:ID! Event_Type:CALL! CALLER_NAME:NAME! CALLER_ADDR:ADDR2! TIME:TIME! Comments:INFO");
    setupCityValues(CITY_CODES);
  }
  
  @Override
  public String getFilter() {
    return "jfussinger@bcpscc.org";
  }
  
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals(MARKER)) break;
      
      if (body.startsWith(MARKER)) {
        body = body.substring(MARKER.length()).trim();
        break;
      }
      
      return false;
    } while (false);
    
    return super.parseMsg(body,  data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
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
  
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*) - *(?:SECTOR )?(?:[NSEW]|[NS][EW]) +(.*)");
  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.length() > 0) return;
      int flags = FLAG_ANCHOR_END;
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCity = match.group(2);
        flags |= FLAG_NO_CITY;
      }
      parseAddress(StartType.START_ADDR, flags, field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("SPECIAL ADDRESS COMMENT:")) field = field.substring(27).trim();
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FLOR", "FLORENCE",
      "UNIO", "UNION",
      "WALT", "WALTON",
      "BURL", "BURLINGTON",
      "PETE", "PETERSBURG",
      "ERLA", "ERLANGER",
      "CRIT", "CRITTENDEN",
      "HEBR", "HEBRON",
      "VERO", "VERONA",
      "INDE", "INDEPENDENCE",
      "BCFL", "FLORENCE",
      "BCWA", "WALTON",
      "BCUN", "UNION",
      "GAC",  "GALATIN",
      "GRC",  "GRANT",
      "KEC",  "KENTON",

  }); 
}
