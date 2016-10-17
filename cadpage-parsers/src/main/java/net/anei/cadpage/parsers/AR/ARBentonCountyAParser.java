package net.anei.cadpage.parsers.AR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ARBentonCountyAParser extends FieldProgramParser {
  
  public ARBentonCountyAParser() {
    super(CITY_LIST, "BENTON COUNTY", "AR",
           "ADDR/S ( NAME PHONE/Z ID | NAME ID | ID ) TIME CALL! INFO");
  }
  
  @Override
  public String getFilter() {
    return "OECOperations@bentoncountyar.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Reject any message that does not start with "OECOperations"
    if(!body.startsWith("OECOperations:")) return false;
    
    // Remove the "OECOperations:" 
    int colon = body.indexOf(':');
    if(colon >= 0) {
      body = body.substring(colon+1).trim();    
    }
    
    // Check to see if the subject contains field data and if so, combine with message body
    if(subject.length() > 0 && !subject.equals("Text Message")) {
      body = subject + ":" + body;
    }
    
    // Parse the fields
    String[] fields = body.split(";");
    if (parseFields(fields, 4, data)) return true;
    
    // If parsing fails, return general alert
    return data.parseGeneralAlert(this,body);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("NAME")) return new MyNameField();
    if(name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("TIME")) return new MyTimeField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);

      data.strCity = fixCity(data.strCity);
      setCityState(data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (isCity(field)) {
        field = field.toUpperCase();
        field = fixCity(field);
        if (data.strCity.length() > 0) {
          if (!field.equalsIgnoreCase(data.strCity)) data.strPlace = field;
        } else {
          data.strCity = field;
          setCityState(data);
        }
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "NAME PLACE CITY ST";
    }
  }
  
  private String fixCity(String city) {
    city = city.replace('_',  ' ');
    if (city.endsWith(" CO")) {
      city = city.substring(0,city.length()-3).trim() + " COUNTY";
    }
    return city;
  }
  
  private void setCityState(Data data) {
    String state = CITY_STATE_TABLE.getProperty(data.strCity);
    if (state != null) data.strState = state;
  }
  
  private static final Pattern PTN_TIME = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
  private class MyTimeField extends TimeField {
    
    @Override 
    public void parse(String field, Data data) {
      Matcher m = PTN_TIME.matcher(field);
      if(m.matches()) {
        data.strTime = field;
      }
      else {
        data.strTime = "";
      }
    }
  }
  
  private static final String[] CITY_LIST = new String[] {
    "AVOCA",
    "BELLA VISTA",
    "BENTON COUNTY",
    "BENTONVILLE",
    "BETHEL HEIGHTS",
    "CAVE SPRINGS",
    "CENTERTON",
    "DECATUR",
    "ELM SPRINGS",
    "GARFIELD",
    "GATEWAY",
    "GENTRY",
    "GRAVETTE",
    "HIGHFILL",
    "HINDSVILLE",
    "HIWASSE",
    "LITTLE FLOCK",
    "LOWELL",
    "MAYSVILLE",
    "PEA RIDGE",
    "ROGERS",
    "SILOAM SPRINGS",
    "SPRINGDALE",
    "SPRINGTOWN",
    "SULPHUR SPRINGS",
    "WAR EAGLE",
    
    // Barry County, MO
    "SELIGMAN",
    
    // Delaware County, OK
    "JAY",
    
    "ADAIR CO",
    "ADAIR_CO",
    "ADAIR COUNTY",
    
    "BARRY CO",
    "BARRY_CO",
    "BARRY COUNTY",
    
    "CARROLL CO",
    "CARROLL_CO",
    "CARROLL COUNTY",
    
    "DELAWARE CO",
    "DELAWARE_CO",
    "DELAWARE COUNTY",
    
    "MADISON CO",
    "MADISON_CO",
    "MADISON COUNTY",
    
    "MCDONALD CO",
    "MCDONALD_CO",
    "MCDONALD COUNTY",
    
    "PETTIS CO",
    "PETTIS_CO",
    "PETTIS COUNTY",
    
    "WASHINGTON CO",
    "WASHINGTON_CO",
    "WASHINGTON COUNTY"
  };
  
  private static final Properties CITY_STATE_TABLE = buildCodeTable(new String[]{
      "BARRY COUNTY",         "MO",
      "MCDONALD COUNTY",      "MO",
      "SELIGMAN",             "MO",
      
      "ADAIR COUNTY",         "OK",
      "JAY",                  "OK",
      "DELAWARE COUNTY",      "OK"
  });
}
