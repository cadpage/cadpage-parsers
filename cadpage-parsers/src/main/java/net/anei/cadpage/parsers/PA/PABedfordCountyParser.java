package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PABedfordCountyParser extends DispatchH05Parser {
  
  public PABedfordCountyParser() {
    super("BEDFORD COUNTY", "PA", 
          "BEDFORD_COUNTY%EMPTY MASH1 ADDR_ID TIMES! TIMES+? GPS!");
    setAccumulateUnits(true);
  }
  
  @Override
  public String getFilter() {
    return "ADnoreply@bedfordcountypa.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MASH1")) return new MyMash1Field();
    if (name.equals("ADDR_ID")) return new MyAddressIdField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private static final Pattern MASH1_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d) (?:(.*) )?CFS: \\d+ +(.*)");
  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*?) *(\\(\\d{3}\\) ?\\d{3}-\\d{4})");
  private class MyMash1Field extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = MASH1_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      String name = getOptGroup(match.group(3));
      data.strCall = match.group(4);
      
      match = NAME_PHONE_PTN.matcher(name);
      if (match.matches()) {
        name = match.group(1);
        data.strPhone = match.group(2);
      }
      data.strName = name;
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME NAME PHONE CALL";
    }
  }
  
  private static final Pattern CLEAN_ID_PTN = Pattern.compile(" *\\[Incident not yet created \\d+\\],?");
  private class MyAddressIdField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('[');
      if (pt >= 0) {
        data.strCallId = cleanIdField(field.substring(pt));
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    private String cleanIdField(String field) {
      return CLEAN_ID_PTN.matcher(field).replaceAll("");
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ID";
    }
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("http://maps.google.com/maps\\?q=loc:(.*)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      field = match.group(1).replace("+-", ",-");
      super.parse(field, data);
      return true;
    }
  }
}
