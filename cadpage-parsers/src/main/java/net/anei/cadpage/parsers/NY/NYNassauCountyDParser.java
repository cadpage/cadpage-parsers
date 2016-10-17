package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYNassauCountyDParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile("^([^\\*]*?)\\*\\*\\*([^\\*]+?)\\*[\\* ]\\* ?");
  private static final Pattern MISSING_DELIM = Pattern.compile("(?<=[^\\*])(?=TOA:)|(?<= \\d\\d-\\d\\d-\\d\\d)(?! ?\\*)");
  private static final Pattern DELIM = Pattern.compile("(?<![\\*\n])\\*");
  
  public NYNassauCountyDParser() {
    super("NASSAU COUNTY", "NY",
           "PLACE? ADDR/ZSXa! CS:X? TOA:TIMEDATE? UNITID? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "eastmeadowfd@eastmeadowfd.net,paging1@firerescuesystems.xohost.com,wantaghpaging@gmail.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    if (name.equals("UNITID")) return new MyUnitIdField();
    return super.getField(name);
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strCall = append(match.group(2).trim(), " - ", match.group(1).trim());
    body = body.substring(match.end());
    body = MISSING_DELIM.matcher(body).replaceAll(" *");
    
    if (body.contains(" c/s:") || body.contains(" ADTNL:") || body.contains(" ADTML:")) return false;
    return parseFields(DELIM.split(body), 2, data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      // If the next field has a colon, it cannot be an address
      // so this field cannot be a place field
      String nextFld = getRelativeField(+1);
      if (nextFld.contains(":")) return false;
      super.parse(field, data);
      return true;
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // the presence of a "CS:" sequence indicates this is really 
      // a NYNassauCountyJ message
      if (field.contains("CS:")) abort();
      super.parse(field, data);
    }
  }
  
  private class MyTimeDateField extends TimeDateField {
    
    public MyTimeDateField() {
      super("\\d\\d:\\d\\d \\d\\d/\\d\\d/\\d\\d", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace('-', '/');
      super.parse(field, data);
    }
  }
  
  private static final Pattern UNIT_ID_PTN = Pattern.compile("(.*)(\\d{4}-\\d{6})");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<=[^ ])(?=RESCUE|ENGINE|LADDER)");
  private class MyUnitIdField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_ID_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strUnit = MISSING_BLANK_PTN.matcher(match.group(1).trim()).replaceAll(",");
      data.strCallId = match.group(2).trim();
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT ID";
    }
    
  }
  
  private static final Pattern INFO_UNIT_PTN = Pattern.compile(" +UNITS +([0-9, /]+)$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_UNIT_PTN.matcher(field);
      if (match.find()) {
        String unit = match.group(1).trim();
        if (unit.endsWith(",")) unit = unit.substring(0,unit.length()-1).trim();
        data.strUnit = append(data.strUnit, ", ", unit);
        field = field.substring(0,match.start());
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO UNIT";
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    if (addr.startsWith("Pre-Plan ")) addr = addr.substring(9).trim();
    addr = BLVE_PTN.matcher(addr).replaceAll(" BLVD");
    return addr;
  }
  private static final Pattern BLVE_PTN = Pattern.compile(" +BLVE\\b");
}


