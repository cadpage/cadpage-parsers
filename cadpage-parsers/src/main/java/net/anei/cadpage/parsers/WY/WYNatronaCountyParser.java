package net.anei.cadpage.parsers.WY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class WYNatronaCountyParser extends FieldProgramParser {
  
  public WYNatronaCountyParser() {
    super("NATRONA COUNTY", "WY",
           "SRC CALL ADDR MAP UNIT INFO+");
  }
  
  @Override
  public String getFilter() {
    return "csphiplink@cityofcasperwy.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equalsIgnoreCase("Message From Hiplink")) return false;
    return parseFields(body.split("\n"), 4, data);
  }
  
  private static final Pattern ADDR_EXT_PTN = Pattern.compile("(?:APT|LOT|SUITE|STE|ROOM|RM|#)[ #]*(.*)|(\\d+[A-Z]?)|(M[MP] .*|[NSEW]B)");
  private static final Pattern COMMENTS_PTN = Pattern.compile("CMNTS|COMMENTS", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String[] flds = field.split(";");
      parseAddress(flds[0], data);
      for (int ndx = 1; ndx < flds.length; ndx++) {
        String fld = flds[ndx].trim().toUpperCase();
        Matcher match = ADDR_EXT_PTN.matcher(fld);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = match.group(2);
          if (apt != null) {
            data.strApt = append(data.strApt, "-", apt);
          } else {
            data.strAddress = data.strAddress + ' ' + fld;
          }
        } else if (! COMMENTS_PTN.matcher(fld).matches()) {
          data.strPlace = append(data.strPlace, " - ", fld);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE APT";
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      if (field.startsWith("PD-")) data.strCity = "CASPER";
      else if (field.startsWith("NC")) data.strCity = "NATRONA COUNTY";
      else {
        String city = CITY_CODES.getProperty(field);
        if (city != null) data.strCity = city;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CITY MAP";
    }
  }

  private static final Pattern CALLBACK_PTN = Pattern.compile("CALLBACK=([^ ]*?) LAT=([^ ]*?) LON=([^ ]*?) UNC=.*");
  private static final Pattern CHANNEL_PTN = Pattern.compile("[\\* ]*(TAC +\\d+)[-\\* ]*(.*)", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALLBACK_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2) + ' ' + match.group(3), data);
        return;
      }
      match = CHANNEL_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PHONE GPS CH INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{3,4}");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALRD",   "ALCOVA LAKE",
      "MWRD",   "MIDWEST",
  });
}
