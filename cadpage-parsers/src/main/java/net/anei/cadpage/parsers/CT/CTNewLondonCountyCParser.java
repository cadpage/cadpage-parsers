package net.anei.cadpage.parsers.CT;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Groton, CT
 */

public class CTNewLondonCountyCParser extends FieldProgramParser {
  
  public CTNewLondonCountyCParser() {
    super(CITY_CODES, "NEW LONDON COUNTY", "CT",
          "( ID CALL | CALL ID? ) PLACE ( ADDRCITY/Z SRCX TIME " + 
                                       "| ADDRCITY/Z APT/Z SRCX TIME " + 
                                       "| ADDR APT CITY MAP_X UNIT DATETIME_INFO " + 
                                       ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "Mobiletec@town.groton.ct.us,mobiletec@groton-ct.gov,NexgenAlerts@groton-ct.gov";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\nDisclaimer");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\\|"), 4, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("SRCX")) return new SourceCrossField();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP_X")) return new MyMapCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME_INFO")) return new MyDateTimeInfoField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) {
        parseAddress(data.strCross, data);
        data.strCross = "";
      } else {
        super.parse(field, data);
      }
    }
  }
  
  private static final Pattern APT_PREFIX_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|STE|SUITE|UNIT)[ #]+", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("APT")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "APT");
      Matcher match = APT_PREFIX_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field,  data);
    }
  }
  
  private static final Pattern SRC_CROSS_PTN = Pattern.compile("(?:STA )?([ A-Z0-9]+) XS(?: (.*))?");
  private class SourceCrossField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      field = field.replaceAll("  +", " ");
      Matcher match = SRC_CROSS_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strSource = match.group(1).replace(' ', '_');
      String cross = match.group(2);
      if (cross != null) data.strCross = cross;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "SRC X";
    }
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("(\\d\\d:\\d\\d)\\b *(.*)");
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strTime = match.group(1);
      data.strSupp = match.group(2).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "TIME INFO";
    }
  }
  
  private static final Pattern LEAD_ZERO_PTN = Pattern.compile("0+");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripLeadZero(field);
      String place = stripLeadZero(data.strPlace);
      if (field.equals(place)) data.strPlace = "";
      super.parse(field, data);
    }
    
    private String stripLeadZero(String field) {
      Matcher match = LEAD_ZERO_PTN.matcher(field);
      if (match.matches()) field = field.substring(match.end()).trim();
      return field;
    }
  }
  
  private static final Pattern MAP_X_PTN = Pattern.compile("(?:Prem )?Map -(\\S+)(?: +(.*))?", Pattern.CASE_INSENSITIVE);
  private class MyMapCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0)  return;
      Matcher match = MAP_X_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strMap = match.group(1);
      super.parse(getOptGroup(match.group(2)), data);
    }
    
    @Override
    public String getFieldNames() {
      return "MAP X";
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_INFO_PTN = Pattern.compile("(\\d\\d-\\d\\d-\\d{4}) (\\d\\d:\\d\\d:\\d\\d)\\b *");
  private class MyDateTimeInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_INFO_PTN.matcher(field);
      if (!match.lookingAt()) abort();
      data.strDate = match.group(1).replace('-', '/');
      data.strTime = match.group(2);
      field = field.substring(match.end());
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME " + super.getFieldNames();
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("NARR")) field = field.substring(4).trim();
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "GRTN CITY",  "GROTON CITY",
      "GRTN TOWN",  "GROTON",
      "GRTN LGPT",  "GROTON",
      "LED",        "LEDYARD",
      "MYSTIC",     "MYSTIC",
      "NOANK",      "NOANK",
      "NSTON",      "NORTH STONINGTON",
      "STON",       "STONINGTON",
      "STON MYST",  "MYSTIC"

  });
}
