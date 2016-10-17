package net.anei.cadpage.parsers.MN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNKandiyohiCountyParser extends FieldProgramParser {
  
  public MNKandiyohiCountyParser() {
    super(CITY_LIST, "KANDIYOHI COUNTY", "MN", 
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR/S! CITY:CITY? ID:ID! PRI:PRI? INFO:INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("[")) {
        int pt = field.lastIndexOf(']');
        if (pt >= 0) {
          data.strPlace = field.substring(1,pt).trim();
          field = field.substring(pt+1).trim();
        }
      }
      super.parse(field, data);
      data.strAddress = stripFieldEnd(data.strAddress, " BLDG");
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0 && !NO_MAP_CITIES.contains(data.strCity.toUpperCase())) return;
      super.parse(field, data);
    }
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "ATWATER",
    "BLOMKEST",
    "KANDIYOHI",
    "LAKE LILLIAN",
    "NEW LONDON",
    "PENNOCK",
    "PRINSBURG",
    "RAYMOND",
    "REGAL",
    "SPICER",
    "SUNBURG",
    "WILLMAR",

    // Townships
    "ARCTANDER",
    "BURBANK",
    "COLFAX",
    "DOVRE",
    "EAST LAKE LILLIAN",
    "EDWARDS",
    "FAHLUN",
    "GENNESSEE",
    "GREEN LAKE",
    "HARRISON",
    "HOLLAND",
    "IRVING",
    "KANDIYOHI",
    "LAKE ANDREW",
    "LAKE ELIZABETH",
    "LAKE LILLIAN",
    "MAMRE",
    "NEW LONDON",
    "NORWAY LAKE",
    "ROSELAND",
    "ROSEVILLE",
    "ST JOHNS",
    "WHITEFIELD",
    "WILLMAR",

    // Unincorporated communities
    "HAWICK",
    "PRIAM",
    "ROSELAND",
    
    // Neighborhoods
    "ALMOND",
    
    // Big Stone County
    "BEARDSLEY",
    "FOSTER"
  };
  
  private static final Set<String> NO_MAP_CITIES = new HashSet<String>(Arrays.asList(new String[]{
      "ALMOND",
      "FOSTER"
  }));
}
