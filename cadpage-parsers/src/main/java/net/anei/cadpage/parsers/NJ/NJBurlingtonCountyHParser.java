package net.anei.cadpage.parsers.NJ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class NJBurlingtonCountyHParser extends DispatchH05Parser {
  
  public NJBurlingtonCountyHParser() {
    super(CITY_CODES, "BURLINGTON COUNTY", "NJ", 
          "RADIO_CHANNEL:CH? TYPE:CALL! DATE:DATETIME! INC_NUMBER:ID! COMMON_NAME:PLACE! ADDRESS:ADDRCITY! \"LOCAL_INFO\":PLACE! CROSS_STREETS:X! NAME:NAME! ADDRESS:SKIP! PHONE:PHONE! NARRATIVE:EMPTY! INFO_BLK+ NATURE:EMPTY? ALERTS:ALERT! FINAL_REPRT:SKIP! https:QUERY!");
    setupMultiWordStreets("REV DR ML KING JR");
  }
  
  @Override
  public String getFilter() {
    return "@co.burlington.nj.us";
  }
  
  private static final Pattern SPEC_DELIM = Pattern.compile("(?:=20)*\n|(?<=\\b\\d\\d:\\d\\d:\\d\\d) (?=[A-Z0-9]+\\\\)");
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    if (subject.equals("Station 261")) {
      data.strSource = subject;
      return parseFields(SPEC_DELIM.split(body), data);
    }
    
    if (subject.startsWith("[")) {
      int pt = subject.indexOf(']');
      if (pt < 0) return false;
      subject = subject.substring(pt+1).trim();
    }
    return super.parseHtmlMsg(subject, body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("QUERY")) return new MyQueryField();
    return super.getField(name);
  }
  
  
  private static final Pattern CITY_PTN = Pattern.compile("(\\d\\d) *(.*)");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE|UNIT) *(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String city = null;
      int pt = field.indexOf(',');
      if (pt >= 0) {
        city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      field = field.replace('@', '&');
      int flags = FLAG_RECHECK_APT | FLAG_ANCHOR_END;
      if (city != null) flags |= FLAG_NO_CITY;
      parseAddress(StartType.START_ADDR, flags, field, data);
      
      if (city != null) {
        city = stripFieldStart(city, data.strApt);
        Matcher match = CITY_PTN.matcher(city);
        if (match.matches()) {
          data.strCity = convertCodes(match.group(1), CITY_CODES);
//          if (NUMERIC.matcher(data.strCity).matches()) abort();
          city = match.group(2);
        }
        if (city.length() > 0) {
          match = APT_PTN.matcher(city);
          if (match.matches()) city = match.group(1);
          if (!city.equals(data.strApt)) data.strApt = append(data.strApt, "-", city);
        }
      }
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }
  
  private static final Pattern QUERY_PTN = Pattern.compile("//www.google.com/maps/.*?(?:,([A-Z+]{3,}))?(?:,([A-Z]{2}))?");
  private class MyQueryField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = QUERY_PTN.matcher(field);
      if (match.matches()) {
        if (data.strCity.length() == 0 || NUMERIC.matcher(data.strCity).matches()) {
          String city = match.group(1);
          if (city != null) {
            city = city.trim().replace('+', ' ');
            data.strCity = stripFieldEnd(city, " BORO");
          }
        }
        data.strState = getOptGroup(match.group(2));
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("EXIT")) return true;
    return super.isNotExtraApt(apt);
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = CL_PTN.matcher(addr).replaceAll("CIR");
    addr = TN_PTN.matcher(addr).replaceAll("TURN");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern CL_PTN = Pattern.compile("\\bCL\\b");
  private static final Pattern TN_PTN = Pattern.compile("\\bTN\\b", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "10", "MAPLE SHADE TWP",
      "11", "DELANCO TWP",
      "13", "LUMBERTON TWP",
      "14", "EDGEWATER PARK TWP",
      "16", "WILLINGBORO TWP",
      "17", "SOUTHAMPTON TWP",
      "18", "PEMBERTON TWP",
      "19", "PEMBERTON",
      "20", "CINNAMINSON TWP",
      "21", "COLUMBUS",
      "22", "EVESHAM TWP",
      "23", "DELRAN TWP",
      "24", "RIVERTON",
      "25", "MEDFORD TWP",
      "26", "CHESTERFIELD TWP",
      "27", "WESTAMPTON TWP",
      "28", "SHAMONG TWP",
      "29", "WOODLAND TWP",
      "30", "BURLINGTON TWP",
      "31", "MOORESTOWN TWP",
      "32", "BORDENTOWN TWP",
      "33", "MANSFIELD TWP",
      "34", "EASTAMPTON TWP",
      "36", "MOUNT LAUREL TWP",
      "37", "MEDFORD LAKES",
      "39", "HAINESPORT TWP",
      "40", "ROEBLING",
      "41", "WRIGHTSTOWN",
      "42", "TUCKERTON",
      "44", "FIELDSBORO",
      "43", "TABERNACLE TWP",
      "50", "MOUNT HOLLY TWP",
      "60", "BORDENTOWN",
      "70", "RIVERSIDE TWP",
      "80", "PALMYRA",
      "90", "BURLINGTON"
      
  });

}
