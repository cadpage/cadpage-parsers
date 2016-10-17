package net.anei.cadpage.parsers.IA;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IAWoodburyCountyParser extends FieldProgramParser {
  
  public IAWoodburyCountyParser() {
    super(CITY_LIST, "WOODBURY COUNTY", "IA", 
         "CALL CALL+? ADDR/SXP! X? SRC UNIT! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "@sioux-city.org";
  }


  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.startsWith("J:")) {
      int pt = body.indexOf("J:");
      if (pt < 0) return false;
      body = body.substring(pt+2).trim();
    }
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("/"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]+", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Result res = parseAddress(StartType.START_ADDR, field);
      if (res.getCity().length() == 0) return false;
      res.getData(data);
      if (SD_CITY_SET.contains(data.strCity.toUpperCase())) data.strState = "SD"; 
      data.strPlace = res.getLeft();
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("&")) return false;
      field = stripFieldEnd(field, "&");
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final String[] CITY_LIST = new String[] {
    
    "WOODBURY COUNTY",
    
    "NORTH SIOUX",
    "DAKOTA DUNES",
    "WYNSTONE",
    "ANTHON",
    "BRONSON",
    "CORRECTIONVILLE",
    "CUSHING",
    "DANBURY",
    
    "HORNICK",
    "LAWTON",
    "MOVILLE",
    "OTO",
    "PIERSON",
    "SALIX",
    "SERGEANT BLUFF",
    "SIOUX CITY",
    "SLOAN",
    "SMITHLAND",
    
    "UNION COUNTY"
  };
  
  private static final Set<String> SD_CITY_SET = new HashSet<String>(Arrays.asList(new String[] {
    "NORTH SIOUX",
    "DAKOTA DUNES",
    "WYNSTONE",
    "UNION COUNTY"
  }));
 

}