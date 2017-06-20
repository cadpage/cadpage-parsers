package net.anei.cadpage.parsers.AR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class ARBentonCountyAParser extends DispatchSouthernParser {
  
  public ARBentonCountyAParser() {
    super(CITY_LIST, "BENTON COUNTY", "AR",
          DSFLG_ADDR | DSFLG_NAME | DSFLG_OPT_PHONE | DSFLG_ID | DSFLG_TIME);
  }
  
  @Override
  public String getFilter() {
    return "OECOperations@bentoncountyar.gov";
  }
  
  private static final Pattern NAME_DISPATCH_PTN = Pattern.compile("(.* CO(?:UNTY)?)(?: DISPATCH)?", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Reject any message that does not start with "OECOperations"
    if(!body.startsWith("OECOperations:")) return false;
    
    // Remove the "OECOperations:" 
    int colon = body.indexOf(':');
    if(colon >= 0) {
      body = body.substring(colon+1).trim();    
    }
    
    if (!super.parseMsg(body, data)) return false;
    
    if (data.strPhone.equals("0000000000")) data.strPhone = "";
    
    if (data.strCity.length() == 0) {
      Matcher match = NAME_DISPATCH_PTN.matcher(data.strName);
      if (match.matches()) data.strCity = match.group(1);
    }
    
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_ST_PTN = Pattern.compile("(.*?)[, ]+(AR|OK|MO)", Pattern.CASE_INSENSITIVE);
  private static final Pattern FARM_RD_PTN = Pattern.compile(".*\\bFARM RD", Pattern.CASE_INSENSITIVE);
  
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strState = match.group(2).toUpperCase();
      }
      super.parse(field, data);
      
      String state = CITY_STATE_TABLE.getProperty(data.strCity);
      if (state != null) data.strState = state;
      
      if (NUMERIC.matcher(data.strApt).matches() && 
          FARM_RD_PTN.matcher(data.strAddress).matches()) {
        data.strAddress = data.strAddress + ' ' + data.strApt;
        data.strApt = "";
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
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
    "WASHBURN",
    
    // Mcdonald County, MO
    "NOEL",
    
    // Delaware County, OK
    "JAY",
    "COLCORD",
    
    // Washington County
    "FAYETTEVILLE",
    
    "ADAIR CO",
    "ADAIR COUNTY",
    
    "BARRY CO",
    "BARRY COUNTY",
    
    "CARROLL CO",
    "CARROLL COUNTY",
    
    "DELAWARE CO",
    "DELAWARE COUNTY",
    
    "MADISON CO",
    "MADISON COUNTY",
    
    "MCDONALD CO",
    "MCDONALD COUNTY",
    
    "PETTIS CO",
    "PETTIS COUNTY",
    
    "WASHINGTON CO",
    "WASHINGTON COUNTY"
  };
  
  private static final Properties CITY_STATE_TABLE = buildCodeTable(new String[]{
      "BARRY COUNTY",         "MO",
      "MCDONALD COUNTY",      "MO",
      "NOEL",                 "MO",
      "SELIGMAN",             "MO",
      "WASHBURN",             "MO",
      
      "ADAIR COUNTY",         "OK",
      "JAY",                  "OK",
      "DELAWARE COUNTY",      "OK",
      "COLCORD",              "OK"
  });
}
