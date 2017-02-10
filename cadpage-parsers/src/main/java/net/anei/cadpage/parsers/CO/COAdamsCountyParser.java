package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class COAdamsCountyParser extends FieldProgramParser {
  
  private static final Pattern CAD_MARKER = 
        Pattern.compile("^(?:Subject:)?IPS I/Page Notifica(?:tion|\\.\\.\\.) (?:/ )?");
  private static final Pattern INTERSTATE_PTN = Pattern.compile("INTERSTATE \\d+(?: [NSEW]B)?", Pattern.CASE_INSENSITIVE);
  
  public COAdamsCountyParser() {
    super(CITY_TABLE, "ADAMS COUNTY", "CO",
           "ADDR! TYPE_CODE:CALL! CALLER_NAME:NAME! TIME:TIME% Comments:INFO Disp:UNIT");
  }
  
  @Override
  public String getFilter() {
    return "ipspage@adcom911.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() > 0) body = "Subject:" + subject + ' ' + body;

    Matcher match = CAD_MARKER.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.end()).trim();
    if (super.parseMsg(body, data)) {
      if (data.strPlace.length() > 0 && INTERSTATE_PTN.matcher(data.strAddress).matches()) {
        data.strAddress = "";
        parseAddress(data.strPlace, data);
        data.strPlace = "";
      }
      return true;
    }
    
    // Fallback parsing address followed by call description
    setFieldList("CALL PLACE ADDR APT CITY INFO");
    data.initialize(this);
    parseAddress(StartType.START_CALL, FLAG_AT_SIGN_ONLY, body, data);
    if (!isValidAddress()) return false;
    if (data.strCall.length() == 0) data.strCall = getLeft();
    else data.strSupp = getLeft();
    return (data.strCall.length() > 0);
  }
  
  
  private static final Pattern TIME_MARK = Pattern.compile(" *\\b(\\d\\d:\\d\\d:\\d\\d)$");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("E 470", "E-470");
    
      Matcher match = TIME_MARK.matcher(field);
      if (match.find()) {
        data.strTime = match.group(1);
        field = field.substring(0, match.start());
      }
      int pt = field.lastIndexOf(':');
      String sPlace = null;
      String sPlace2 = null;
      if (pt >= 0) {
        sPlace = stripFieldStart(field.substring(pt+1).trim(), "@");
        field = field.substring(0,pt).trim();
      }
      
      if (sPlace != null) {
        pt = field.lastIndexOf(':');
        if (pt >= 0) {
          sPlace2 = sPlace;
          sPlace = stripFieldStart(field.substring(pt+1).trim(), "@");
          field = field.substring(0,pt);
        }
      }
  
      String city = CITY_TABLE.getProperty(field);
      if (city != null) {
        data.strCity = city;
        field = sPlace;
        if (field == null) field = "";
        sPlace = sPlace2;
        sPlace2 = null;
      }
      
      if (sPlace2 != null && !sPlace.equals(sPlace2)) sPlace = append(sPlace, ": ", sPlace2);
      
      pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strApt = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      if (field.startsWith("MM ")) {
        data.strAddress = field;
      } else if (data.strCity.length() > 0) {
        parseAddress(field, data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      }
      if (sPlace != null) {
        String sCross = null;
        pt = sPlace.indexOf('/');
        if (pt >= 0) {
          String sPart1 = sPlace.substring(0,pt).trim();
          String sPart2 = sPlace.substring(pt+1).trim();
          if (sPart1.equals(data.strAddress)) sCross = sPart2;
          else if (sPart2.equals(data.strAddress)) sCross = sPart1; 
        }
        if (sCross != null) data.strAddress = append(data.strAddress, " & ", sCross);
        else data.strPlace = sPlace;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
  }
  
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      if (data.strTime.length() > 0) return;
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PATTERN.matcher(field);
      if (match.find()) {
        setGPSLoc(match.group(), data);
        field = field.substring(match.end()).trim();
      }
      super.parse(field, data);
    }
    
    @Override 
    public String getFieldNames() {
      return "GPS INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return TWO_PT_FIVE.matcher(addr).replaceAll("2 1/2");
  }
  private Pattern TWO_PT_FIVE = Pattern.compile("\\b2\\.5\\b");
  
  private static final Properties CITY_TABLE = buildCodeTable(new String[]{
      "ADAM ADAM", "ADAMS COUNTY",
      "ADAM ARV",  "ARVADA",
      "ADAM AUR",  "AURORA",
      "ADAM BPD",  "BRIGHTON",
      "ADAM CCPD", "COMMERCE CITY",
      "ADAM FHPD", "FEDERAL HEIGHTS",
      "ADAM TPD",  "THORNTON",
      "ADAM NPD",  "NORTHGLENN",
      "ADAM WES",  "WESTMINSTER",
      "ARAP ARAP", "ARAPAHOE COUNTY",
      "JECO ARV",  "ARVADA",
      "WELD BPD",  "BRIGHTON",
      "WELD FTLP", "FORT LUPTON",
      "WELD WELD", "WELD COUNTY"
  });
}
