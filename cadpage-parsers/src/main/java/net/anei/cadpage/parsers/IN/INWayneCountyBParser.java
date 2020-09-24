package net.anei.cadpage.parsers.IN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;

public class INWayneCountyBParser extends DispatchA3Parser {
  
  private HtmlDecoder decoder = new HtmlDecoder("table|tr");
  
  public INWayneCountyBParser () {
    super("WAYNE COUNTY", "IN", 
          "SKIP+? ID! Date/Time_Received:EMPTY! DATETIME! Complaint:EMPTY! CALL! Caller:EMPTY! NAME_PHONE! Address:EMPTY! ADDR! INFO<+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "911@wayneco.us,root@co.wayne.in.us";
  }
  
  private enum InfoType { INFO, UNITS_ASSGN };
  private InfoType infoType;
  private int colNdx;
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("911 Dispatch: ")) return false;
    String[] flds = decoder.parseHtml(body);
    if (flds == null) return false;
    
    infoType = InfoType.INFO;
    colNdx = -1;
    return parseFields(flds, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Call Number (\\d{2}-\\d{8}) .*", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CALL_PATTERN  = Pattern.compile("(\\d{2}\\-\\d{2})\\b *(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PATTERN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Pattern NAME_PHONE_PATTERN = Pattern.compile("(.*?)\\b(\\d{3}\\-\\d{3}\\-\\d{4})");
  private static final Pattern JUNK_PHONE_PATTERN = Pattern.compile("[- \\d]+$");
  private class MyNamePhoneField extends NameField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE_PATTERN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPhone = match.group(2);
      }
      field = JUNK_PHONE_PATTERN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      int pt = field.indexOf('\n');
      if (pt >= 0) field = field.substring(0,pt).trim();
      field = field.replace("//", "/");
      
      pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      else {
        pt = field.lastIndexOf('/');
        if (pt >= 0) {
          String city = field.substring(pt+1).trim();
          if (CITIES.contains(city.toUpperCase())) {
            data.strCity = city;
            field = field.substring(0,pt).trim();
          }
        }
      }
      parseAddress (StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d");
  private class MyInfoField extends BaseInfoField {
    @Override
    public void parse(String field, Data data) {
      
      // Special Html tag processing
      if (field.startsWith("<|") && field.endsWith("|>")) {
        if (field.equals("<|/table|>")) {
          infoType = InfoType.INFO;
          colNdx = -1;
        }
        else if (field.equals("<|tr|>")) {
          colNdx = 0;;
        }
        return;
      }
      
      if (colNdx >= 0) colNdx++;
      
      if (field.equals("Units Assigned")) {
        infoType = InfoType.UNITS_ASSGN;
        colNdx = -1;
        return;
      }
      
      if (field.equals("Case Number:") || field.equals("Complaint Changes") ||
          field.equals("No Units Assigned")) {
        infoType = null;
        return;
      }
      
      if (infoType == null) return;
      switch (infoType) {
      
      case INFO:
        super.parse(field, data);
        return;
      
      case UNITS_ASSGN:
        if (colNdx == 1 && !field.equals("Unit")) {
          if (!INFO_DATE_TIME_PTN.matcher(field).matches()) {
            data.strUnit = append(data.strUnit, " ", field);
          }
        }
        return;
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " UNIT";
    }
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "401 E MAIN ST",                        "+39.828242,-84.895781"
  });
  
  private static final Set<String> CITIES = new HashSet<String>(Arrays.asList(new String[]{
      
    // Counties  
    "RANDOLPH",
    "DARKE",
    "PREBLE",
    "UNION",
    "FAYETTE",
    "HENRY",
    
    // Cities and towns
    "BOSTON",
    "CAMBRIDGE CITY",
    "CENTERVILLE",
    "DUBLIN",
    "EAST GERMANTOWN",
    "ECONOMY",
    "FOUNTAIN CITY",
    "GREENS FORK",
    "GREENSFORK",   
    "HAGERSTOWN",
    "MILTON",
    "RICHMOND",
    "SPRING GROVE",
    "WHITEWATER",
    
    // Unincorporated towns
    "ABINGTON",
    "BETHEL",
    "JACKSONBURG",
    "MIDDLEBORO",
    "PENNVILLE",
    "WEBSTER",
    "WILLIAMSBURG",
    
    // Townships
    "ABINGTON",
    "BOSTON",
    "CENTER",
    "CLAY",
    "DALTON",
    "FRANKLIN",
    "GREEN",
    "HARRISON",
    "JACKSON",
    "JEFFERSON",
    "NEW GARDEN",
    "PERRY",
    "WASHINGTON",
    "WAYNE",
    "WEBSTER",
    
    // Fulton County???
    "PERSHING",
    
    // Union County
    "BROWNSVILLE"
  }));
}
