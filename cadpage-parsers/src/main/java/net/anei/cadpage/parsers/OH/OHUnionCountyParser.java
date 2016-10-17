package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHUnionCountyParser extends FieldProgramParser {
  
  public OHUnionCountyParser() {
    super(CITY_LIST, "UNION COUNTY", "OH",
           "CALL ADDR/S ( CITY/Z ST_ZIP | CITY | ) X/Z+? X2! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "idnetworks@co.union.oh.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!body.endsWith(",")) data.expectMore = true;
    return parseFields(body.split(","), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ST_ZIP")) return new SkipField("OH(?: +\\d{5})?", true);
    if (name.equals("X2")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf("//");
      if (pt < 0) return false;
      String p1 = field.substring(0,pt).trim();
      String p2 = field.substring(pt+2).trim();
      if (!p1.equals(data.strCity)) super.parse(p1, data);
      super.parse(p2, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.startsWith(field)) return;
      super.parse(field, data);
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "DUBLIN",
    "MARYSVILLE",
    
    // Villages
    "MAGNETIC SPRINGS",
    "MILFORD CENTER",
    "PLAIN CITY",
    "RICHWOOD",
    "UNIONVILLE CENTER",
    
    // Unincorporated communities
    "ALLEN CENTER",
    "ARNOLD",
    "BRIDGEPORT",
    "BROADWAY",
    "BYHALIA",
    "CHUCKERY",
    "CLAIBOURNE",
    "DIPPLE",
    "ESSEX",
    "IRWIN",
    "JEROME",
    "LUNDA",
    "NEW CALIFORNIA",
    "NEW DOVER",
    "PEORIA",
    "PHARISBURG",
    "POTTERSBURG",
    "RAYMOND",
    "SOMERSVILLE",
    "WATKINS",
    "WOODLAND",
    "YORK CENTER",
    
    // Townships
    "ALLEN TWP",
    "CLAIBOURNE TWP",
    "DARBY TWP",
    "DOVER TWP",
    "JACKSON TWP",
    "JEROME TWP",
    "LEESBURG TWP",
    "LIBERTY TWP",
    "MILLCREEK TWP",
    "PARIS TWP",
    "TAYLOR TWP",
    "UNION TWP",
    "WASHINGTON TWP",
    "YORK TWP"
  };
}
