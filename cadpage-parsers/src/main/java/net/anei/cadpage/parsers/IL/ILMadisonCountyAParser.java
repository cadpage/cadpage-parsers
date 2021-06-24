package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;


/**
 * Madison County, IL
 */
public class ILMadisonCountyAParser extends DispatchH05Parser {
  
  public ILMadisonCountyAParser() {
    super("MADISON COUNTY", "IL",
          "( NEW_SRC Fire_Call_Type:CALL! Call_Address:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! Nature_of_Call:CALL/SDS! Narrative:INFO_BLK! INFO_BLK+ Call_Date/Time:SKIP! Caller_Name:NAME! Caller_Phone_#:PHONE! Status_Times:TIMES! TIMES+ Incident_Number:ID! Fire_Quadrant:SKIP! EMS_District:SKIP! Google_Map_Hyperlink:SKIP! " +
          "| CALL_RECEIVED_AT? CALL ( PLACE ADDRCITY/S6 ID? X? " + 
                                   "| ADDRCITY/S6 ( ID X? | ( PLACE X | X | PLACE X/L | X/L | ) ) " +
                                   "| ADDRCITY/ZS6 PLACE X " + 
                                   "| ADDRCITY/ZS6 X " + 
                                   "| ADDRCITY/ZS6 PLACE X/L " + 
                                   "| ADDRCITY/ZS6 X/L " + 
                                   "| X/S " +
                                   "| DATE_MARK " +
                                   "| ADDRCITY/S6 " +
                                   ") INFO_BLK/I+? ( DATETIME_MARK ( PHONE | NAME PHONE | ) | ID? ) TIMES+ https:QUERY " + 
         ")");
    setupSpecialStreets("BROADWAY", "TERRA VERDE");
  }
  
  @Override
  public String getFilter() {
    return "@glen-carbon.il.us,@co.madison.il.us,@troypolice.us,@cityofedwardsville.com,@highlandil.gov,@siue.edu,@collinsvilleil.org,@granitecity.illinois.gov,ripnrun@madisonco.com";
  }

  private static final Pattern FIND_ID_PTN = Pattern.compile("\\[(?:(\\d{4}-\\d{8})|Incident not yet created) ([A-Z]+\\d+)\\][ ,]*");
  
  @Override
  public Field getField(String name) {
    if (name.equals("NEW_SRC")) return new SourceField("(.*?) R&R from New World Systems", true);
    if (name.equals("CALL_RECEIVED_AT")) return new SkipField("(?i)Call Rece?ie?ved at", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("DATE_MARK")) return new SkipField("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}", true);
    if (name.equals("DATETIME_MARK")) return new MyDateTimeMarkField();
    if (name.equals("INFO_BLK")) return new MyInfoBlockField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("PHONE")) return new PhoneField("\\(\\d{3}\\) \\d{3}-\\d{4}", false);
    if (name.equals("QUERY")) return new MyQueryField();
    return super.getField(name);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(getRelativeField(-1))) return;
      if (field.equals("<UNKNOWN>")) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern ADDR_CITY_PTN = Pattern.compile(".*, *[A-Z][a-z][A-Za-z ]+");
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!ADDR_CITY_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private static final Pattern NOT_CROSS_PTN = Pattern.compile("\\*.*\\*|.*:.*|.*[a-z].*");
  private class MyCrossField extends CrossField {
    
    private boolean loose = false;
    private boolean strict = false;
    
    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      if (qual != null) {
        loose = qual.contains("L");
        strict = qual.contains("S");
      }
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      field = field.replace("PRIVATE_LANE", "PRIVATE LANE");
      if (field.equals("No Cross Streets Found")) return true;
      if (field.contains(";")) return false;
      if (NOT_CROSS_PTN.matcher(field).matches()) return false;
      boolean good;
      if (loose) {
        good = field.contains("/");
      } else if (strict) {
        if (!field.contains("/")) return false;
        String temp = field;
        int pt = temp.lastIndexOf(',');
        if (pt >= 0) temp = temp.substring(pt+1).trim();
        good = isValidCrossStreet(temp);
      } else {
        good = false;
        for (String part : field.split(",")) {
          part = part.trim();
          if (isValidCrossStreet(part)) {
            good = true;
            break;
          }
        }
      }
      if (good) {
        super.parse(field, data);
        return true;
      }
      return false;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_MARK_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d");
  
  private class MyDateTimeMarkField extends SkipField {
    public MyDateTimeMarkField() {
      setPattern(DATE_TIME_MARK_PTN, true);
    }
  }
  
  private class MyInfoBlockField extends BaseInfoBlockField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (DATE_TIME_MARK_PTN.matcher(field).matches()) return false;
      if (FIND_ID_PTN.matcher(field).matches()) return false;
      return super.checkParse(field, data);
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      String result = "";
      for (String part : field.split(", *")) {
        Matcher match = FIND_ID_PTN.matcher(part);
        if (!match.matches()) return false;
        String id = match.group(1);
        if (id != null) result = append(result, ", ", id + ' ' + match.group(2));
      }
      data.strCallId = append(data.strCallId, ", ", result);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }
  
  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");
  private class MyQueryField extends Field {
    @Override
    public void parse(String field, Data data) {
      
      // This is a URL to bring up the google map address.  Generally we ignore it,
      // but some calls are missing the normal address, in which case we will try to
      // extract an address from the query string
      if (data.strAddress.length() > 0) return;
      
      int pt = field.indexOf("query=");
      if (pt < 0) return;
      field = field.substring(pt+6).trim().replace('+', ' ');
      
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (STATE_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      
      field = p.get().replace('@', '&');
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
}
