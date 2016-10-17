package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;

/**
 * Isanti County, MN
 */

public class MNIsantiCountyParser extends FieldProgramParser {

  public MNIsantiCountyParser() {
    super("ISANTI COUNTY", "MN",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! INFO:INFO+");
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
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
      
      // There is not always a blank between the address and a trailing
      // city.  There always seems to be a trailing city, which may or
      // may not match the following CITY: parameter.  All of which is
      // quite confusing, but it does appear that the CITY: parameter
      // field is the more specific one, so we will let it override
      // the one in the address.
      String city = CITY_SET.getCode(field.toUpperCase());
      if (city != null) {
        int pt = field.length() - city.length();
        data.strCity = field.substring(pt);
        field = field.substring(0,pt).trim();
      } 
      
      // Uncoment the following for internal testing
      // else abort();
      super.parse(field, data);
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
      
      // Uncomment this to make sure we have all of the cities identified
      // if (CITY_SET.getCode(field.toUpperCase()) == null) abort();
    }
  }
  
  private static final ReverseCodeSet CITY_SET = new ReverseCodeSet(
  
      // Cities
      "BRAHAM",
      "CAMBRIDGE",
      "ISANTI",
  
      // Townships
      "ATHENS",
      "BRADFORD",
      "CAMBRIDGE",
      "DALBO",
      "ISANTI",
      "MAPLE RIDGE",
      "NORTH BRANCH",
      "OXFORD",
      "SPENCER BROOK",
      "SPRINGVALE",
      "STANCHFIELD",
      "STANFORD",
      "WYANETT",
  
      // Unincorporated communities
      "ANDREE",
      "ATHENS",
      "BLOMFORD",
      "BODUM",
      "BRADFORD",
      "CARMODY",
      "CROWN",
      "DALBO",
      "DAY",
      "EDGEWOOD",
      "ELM PARK",
      "GRANDY",
      "OXLIP",
      "PINE BROOK",
      "SPENCER BROOK",
      "SPRING LAKE",
      "SPRINGVALE",
      "STANLEY",
      "STANCHFIELD",
      "STANFORD",
      "WALBO",
      "WEBER",
      "WEST POINT",
      "WYANETT",
      
      // Mile Lacs County
      "PRINCETON",
      "SPENCER BROOK",
      
      // Not sure what this is, but we need to get rid of it
      "UPPER"
  );
}
