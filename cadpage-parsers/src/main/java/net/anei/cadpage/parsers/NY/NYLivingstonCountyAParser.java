package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchA5Parser;

import java.util.Properties;
import java.util.regex.*;

public class NYLivingstonCountyAParser extends DispatchA5Parser {
  
  public NYLivingstonCountyAParser() {
    super(CITY_CODES, STANDARD_CODES, "LIVINGSTON COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "@CO.LIVINGSTON.NY.US";
  }
  
  @Override 
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }
  
  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    
    if(!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("COUNTY OUT")) {
      data.strCity = "";
      data.defCity = "";
    }
    return true;
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('/');
      if (pt >= 0) field = field.substring(pt+1).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField() ;
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    
    sAddress = MA_PTN.matcher(sAddress).replaceAll("MANOR");
    sAddress = EX_PTN.matcher(sAddress).replaceAll("EXPY");
    sAddress = IFO_PTN.matcher(sAddress).replaceAll("");
    return sAddress;
  }
  private static final Pattern MA_PTN = Pattern.compile("\\bMA\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern EX_PTN = Pattern.compile("\\bEX\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern IFO_PTN = Pattern.compile(" +IFO$", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "GROVELAN",   "GROVELAND",
      "SPRINGWA",   "SPRINGWATER",
  });
  
  private static final StandardCodeTable STANDARD_CODES = new StandardCodeTable(); 
}
