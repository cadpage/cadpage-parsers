package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIBerrienCountyParser extends FieldProgramParser {

  public MIBerrienCountyParser() { 
    super(CITY_LIST, "BERRIEN COUNTY", "MI",
          "Location:ADDR! Common_Name:PLACE! Call_Type:CALL! Call_Time:DATETIME! Call_Number:ID! Narrative:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@berriencounty.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("Incident")) return false;
    if (parseMsg(body, data)) return true;
    setFieldList("INFO");
    data.parseGeneralAlert(this, body);
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static Pattern SECTOR_PTN = Pattern.compile("(.*) [NS]?[EW]? SECTOR", Pattern.CASE_INSENSITIVE);
  private static Pattern GPS_PTN = Pattern.compile("(.*) (?:(\\d{2}.\\d{6,} -\\d{2}\\.\\d{6,})|-361 -361)", Pattern.CASE_INSENSITIVE);
  private static Pattern CITY_PTN = Pattern.compile("(.*)(?<!\\bUS|\\bM) \\d{2} (.*[a-z].*)");
  private static Pattern CITY_PTN2 = Pattern.compile("([^a-z]+) ([ A-Za-z]+[a-z][ A-Za-z]+)");
  private static Pattern CITY_COUNTY_PTN = Pattern.compile("(.*?) ((?:CASS|COOK|LA PORTE|LAKE|ST JOSEPH|PORTER|VAN BUREN) CO(?:UNTY)?)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // Parse trailing sector
      Matcher mat = SECTOR_PTN.matcher(field);
      if (mat.matches()) field = mat.group(1).trim();
      
      // Parse trailing GPS
      mat = GPS_PTN.matcher(field);
      if (mat.matches()) {
        field = mat.group(1).trim();
        String gps = mat.group(2);
        if (gps != null) setGPSLoc(gps.trim(), data);
      }
      
      //parse trailing CITY
      mat = CITY_PTN.matcher(field);
      if (mat.matches()) {
        field = mat.group(1).trim();
        data.strCity = mat.group(2).trim();
        field = stripFieldEnd(field, ",");
      }
      
      // Strip out cross street
      field = stripFieldEnd(field, " X");
      int pt = field.indexOf(" X ");
      if (pt >= 0) {
        data.strCross = field.substring(pt+3).replaceAll(" X ", " ").trim();
        field = field.substring(0,pt).trim();
      }
      
      // If we did not find a city before, look for one now
      if (data.strCity.length() == 0) {
        mat = CITY_PTN2.matcher(field);
        if (mat.matches()) {
          field = mat.group(1).trim();
          data.strCity = mat.group(2).trim();
        }
        else {
          pt = field.lastIndexOf(',');
          if (pt >= 0) {
            data.strCity = field.substring(pt+1).trim();
            field = field.substring(0,pt).trim();
          }
        }
      }
      
      // If we still have not found a city, see if the smart address parser
      // will find one.  Otherwise just parse the address
      if (data.strCity.length() == 0) {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      } else {
        field = stripFieldEnd(field, ",");
        parseAddress(field, data);
      }

      // If city consists of a city and county, strip off the county
      // But use the county portion to set the state
      mat = CITY_COUNTY_PTN.matcher(data.strCity);
      if (mat.matches()) {
        data.strCity = mat.group(1).trim();
        setState(mat.group(2), data);
      }
      
      // Make any city name corrections
      String city = FIX_CITY_TABLE.getProperty(data.strCity.toUpperCase());
      if (city != null) data.strCity = city;

      // Use final city name to set state
      setState(data.strCity, data);
    }
    
    private void setState(String city, Data data) {
      city = city.toUpperCase();
      if (city.endsWith(" COUNTY")) city = city.substring(0,city.length()-4);
      String st = CITY_ST_TABLE.getProperty(city);
      if (st != null) data.strState = st;
    }
    
    @Override public String getFieldNames() {
      return super.getFieldNames() + " X CITY ST CITY GPS";
    }
  }
  
  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "NEW CARLIE",       "New Carlisle",
      "NEW CARLYIYSLE",   "New Carlisle",
      "ST JOE",           "St Joseph Co",
      "ST JOE CO",        "St Joseph Co",
      "ST JOE COUNTY",    "St Joseph Co",
  });
  
  private static final Properties CITY_ST_TABLE = buildCodeTable(new String[]{
      "LA PORTE",     "IN",
      "LAPORTE CO",   "IN",
      "LAPORTE",      "IN",
      "NEW CARLISLE", "IN",
      "PORTER CO",    "IN",
      "SOUTH BEND",   "IN",
      "SPRINGFIELD",  "IN",
      "ST JOSEPH CO", "IN",
      "WARREN TWP",   "IN"
  });
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("COGIC", "C O G I C");
    return super.adjustMapAddress(addr);
  }
  
  @Override
  public String adjustMapCity(String city) {
    String tmp = city.toUpperCase();
    if (tmp.endsWith(" TOWNSHIP")) {
      tmp = tmp.substring(0,tmp.length()-9)+" TWP";
    }
    tmp = MAP_CITY_TABLE.getProperty(tmp);
    if (tmp != null) return tmp;
    
    if (city.toUpperCase().endsWith(" CO")) city += "unty";
    return city;
  }
  
  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "COLOMA TWP",         "Coloma Charter Twp",
      "LAKE TWP",           "Lake Charter Twp",
      "LINCOLN TWP",        "Lincoln Charter Twp",
      "ORONOKO TWP",        "Oronoko Charter Twp",
      "ST JOSEPH TWP",      "St Joseph Charter Twp"
  });
  
  // This only includes out of county cities, as they seem to be the
  // only ones that can not be identified by one of our pattern matches.
  private static final String[] CITY_LIST = new String[]{
    
    // Cass County
    "CASS",
    "HOWARD",
    "HOWARD TWP",
    "MILTON",
    
    // La Porte County
    "LAPORTE",
    
    // Van Buren County
    "VAN BUREN COUNTY",
    "COVERT",
    
    // St Joseph County
    "NEW CARLIE",
    "NEW CARLISLE",
    "NEW CARLYIYSLE",
    "SOUTH BEND",
    "WARREN TWP"
  };
}
