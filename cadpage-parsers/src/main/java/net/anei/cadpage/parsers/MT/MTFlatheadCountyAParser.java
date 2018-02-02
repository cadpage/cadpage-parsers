package net.anei.cadpage.parsers.MT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MTFlatheadCountyAParser extends FieldProgramParser {

  private static final String SOURCE_LIST = "(Airport|KFD|Creston FD|Evergreen FD|Olney FD|South Kalispell? +FD|Smith Valley FD|Somers FD|Whitefish ?FD|C\\.FALLS ?FD)";
  private static final Pattern MASTER1 = 
      Pattern.compile("(?:([A-Z0-9_ ]+?) +)?" +
          SOURCE_LIST +
          " *(.*_GPIA|.*?_KIDC|.*? Hydrant (?:Commer|Residential)|.*?(?:_Residential|_Commercial|_Res)\\)|\\([^\\)]+\\))" +
          " +(.*?) +(\\d{4}-\\d{8})");
  private static final Pattern MASTER2 = Pattern.compile("(?:((?:(?:[A-Z0-9]{1,5}|[A-Z]*GENALM) )+) )?" + SOURCE_LIST + "?(.*?)(?:  (.*))??(?: +(\\d{4}-\\d{8}))?");
  
  private static final Pattern MISSED_BLANK_PTN = Pattern.compile("(?<! )(?=Call Type:)");
  
  private static final String CITY_PTN_LIST = "(BIGFORK|CORAM|COLUMBIA FALLS|ESSEX|EVERGREEN|GPIA|HUNGRY HORSE|KILA|KALISPELL|LAKESIDE|MARION|MARTIN CITY|NIARADA|OLNEY|POLEBRIDGE|SOMERS|WEST GLACIER|WHITEFISH|LAKE COUNTY)";
  private static final Pattern CITY_SPACE_PTN = Pattern.compile("(.+)" + CITY_PTN_LIST + " +(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CITY_END_PTN = Pattern.compile(CITY_PTN_LIST + "$", Pattern.CASE_INSENSITIVE);
  
  private static final String  CALL_PTN_STR = "((?:F [A-Z][a-z]|F CO|Medical |MVA).*|Inter Facility Transfer|< New Call >)";
  private static final Pattern CALL_PTN = Pattern.compile(CALL_PTN_STR);
  private static final Pattern ADDR_CALL_PTN = Pattern.compile("(.+)" + CALL_PTN_STR);


  private static final Pattern DIR_SUFFIX_PTN = Pattern.compile("^[EW][NS]\\b");
  private static final Pattern APT_PTN = Pattern.compile("[A-Z]\\d*", Pattern.CASE_INSENSITIVE);

  public MTFlatheadCountyAParser() {
    super("FLATHEAD COUNTY", "MT",
          "( SELECT/2 RESCUE_TYPE_DISPATH%EMPTY Unit:UNIT! Loc:ADDR2!  Type:CALL! INFO/N+ " +
          "| SMS_NOTIFICATION EMPTY! Address:ADDRCITY! Common_Name:PLACE! Venue:CITY! Closest_Intersection:X! Additional_Location_Info:INFO! RP_Phone_Number:PHONE! " + 
              "Quadrant:MAP! District:MAP/L! LAT/LONG:GPS3! EMPTY! CFS_Number:SKIP! Call_Type:CALL! Nature_of_Call:CALL/SLS! Source:SKIP! Priority:PRI! Status:SKIP! " + 
              "Caller:NAME3! Call_Date/Time:DATETIME3! Primary_Incident:ID! Dispatched_Units:UNIT! Units:UNIT/S! UNIT/S+ Incident_Numbers:SKIP! Narrative:INFO! INFO/N+ " +
          "| Unit:UNIT Status:SKIP Location:ADDR1/SXa! Gen:PLACE Call_Type:CALL! Call_Time:TIME! Info:INFO/N+ )");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@communicationcenter.com,911@flatheadoes.mt.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.endsWith(" Type Call")) {
      setSelectValue("2");
      return parseFields(body.split("\n"), data);
    }
    
    if (!subject.equals("Dispatch Information")) return false;

    setSelectValue("1");
    if (body.startsWith("SMS Notification ")) {
      return super.parseFields(body.split("\n"), data);
    }

    // Try the older labeled field format first
    if (body.contains("Call Type:")) {
      body = MISSED_BLANK_PTN.matcher(body).replaceAll(" ");
      return super.parseMsg(body, data);
    }
    
    // Several different formats :(
    // Newest formats have to use master pattern matches
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      
      setFieldList("UNIT SRC INFO ADDR APT PLACE CITY CALL ID");
      
      data.strUnit = getOptGroup(match.group(1));
      data.strSource = match.group(2);
      data.strSupp = match.group(3);
      body = match.group(4);
      data.strCallId = match.group(5);
      
      parseAddressField(body, data);
      return true;
    }
    
    // Another master pattern match format
    // The match test is mostly for show, everything in the pattern is optional
    // so it probably is not possible for it to fail.  We will check later to
    // see if we go enough of the optional values to accept the result
    match = MASTER2.matcher(body);
    if (!match.matches()) return false;
    setFieldList("UNIT SRC ADDR APT PLACE CITY CALL CODE INFO ID");
    data.strUnit = getOptGroup(match.group(1));
    data.strSource = getOptGroup(match.group(2));
    parseAddressField(match.group(3).trim(), data);
    parseInfoField(getOptGroup(match.group(4)), data);
    data.strCallId = getOptGroup(match.group(5));
    
    // Good if we foundn a unit, code, or call ID
    if (data.strUnit.length() > 0 || data.strCode.length() > 0 || data.strCallId.length() > 0) return true;
    
    // No luck there, OK if we have a city and a call description that 
    // looks reasonable
    return (data.strCity.length() > 0 && CALL_PTN.matcher(data.strCall).matches());
  }

  private boolean parseAddressField(String field, Data data) {
    
    // What is left always contains a city, but we have to use pattern  matches
    // because there usually not preceded by a separating blank.
    Matcher match = CITY_SPACE_PTN.matcher(field);
    if (!match.matches()) {
      
      // No city found :(
      // See if we can recognize a break pattern
      match = ADDR_CALL_PTN.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim(), data);
        data.strCall = match.group(2).trim();
        return true;
      }
      data.strCall = field;
      return false;
    }
    field = match.group(1).trim();
    data.strCity = match.group(2);
    data.strCall = match.group(3).trim();
    
    // Now we are down to an address and place/apt
    parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT, field, data);
    String left = getLeft();
    match = DIR_SUFFIX_PTN.matcher(left);
    if (match.find()) {
      data.strAddress = append(data.strAddress, " ", match.group());
      left = left.substring(match.end()).trim();
    }
    if (data.strApt.length() == 0 && APT_PTN.matcher(left).matches()) {
      data.strApt = left;
    } else {
      data.strPlace = left;
    }
    return true;
  }

  private void parseInfoField(String info, Data data) {
    int pt = info.indexOf(" E911 Info");
    if (pt >= 0) info = info.substring(0,pt).trim();
    
    Matcher match = NATURE_OF_CALL_PTN.matcher(info);
    if (match.matches()) {
      data.strCode = match.group(2);
      info = append(match.group(1), " / ", match.group(3));
    }
    while (true) {
      match = DISPATCH_UNIT_PTN.matcher(info);
      if (!match.find()) break;
      data.strUnit = append(data.strUnit, " ", match.group(1));
      info = append(info.substring(0,match.start()), " / ", info.substring(match.end()));
    }
    info = DOUBLE_SLASH_PTN.matcher(info).replaceAll(" / ");
    if (info.endsWith("/")) info = info.substring(0,info.length()-1).trim();
    data.strSupp = append(data.strSupp, "\n", info);
  }
  private static final Pattern NATURE_OF_CALL_PTN = Pattern.compile("(.*?) *\\bNature Of Call: *([A-Z0-9]+)\\b *(.*?)"); 
  private static final Pattern DISPATCH_UNIT_PTN = Pattern.compile(" *\\bDispatch received by unit ([A-Z0-9]+)\\b *");
  private static final Pattern DOUBLE_SLASH_PTN = Pattern.compile(" / (?:/ )+");

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("INFO")) return new MyInfoField();
    
    if (name.equals("SMS_NOTIFICATION")) return new SkipField("SMS Notification \\d{4}", true);
    if (name.equals("GPS3")) return new MyGPS3Field();
    if (name.equals("NAME3")) return new MyName3Field();
    if (name.equals("DATETIME3")) return new MyDateTime3Field();
    return super.getField(name);
  }
  
  private class MyAddress1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("U:")) field = field.substring(2).trim();
      Matcher match = CITY_END_PTN.matcher(field);
      if (match.find()) {
        data.strCity = match.group();
        field = field.substring(0,match.start());
      }
      super.parse(field, data);
      if (NUMERIC.matcher(data.strPlace).matches()) {
        data.strApt = append(data.strApt, "-", data.strPlace);
        data.strPlace = "";
      }
    }
  }
  
  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(" - ");
      if (pt >= 0) {
        data.strCity = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_END_PTN.matcher(field);
      if (match.find()) {
        data.strCity = match.group();
        field = field.substring(0,match.start());
      }
      if (data.strAddress.length() == 0) {
        parseAddress(field, data);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("MAP ")) field = field.substring(4).trim();
      data.strMap = append(data.strMap, " - ", field);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      parseInfoField(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "UNIT CODE INFO";
    }
  }
  
  private static final Pattern GPS3_PTN = Pattern.compile("(?<=\\d)(?=-)");
  private class MyGPS3Field extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = GPS3_PTN.matcher(field).replaceFirst(",");
      super.parse(field, data);
    }
  }
  
  private class MyName3Field extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field,  ",");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_3_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTime3Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_3_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("FARM TO MARKET", "FARM-TO-MARKET");
  }
}
