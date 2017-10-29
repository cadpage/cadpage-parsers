package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHCuyahogaCountyDParser extends FieldProgramParser {

  public OHCuyahogaCountyDParser() {
    super(OHCuyahogaCountyParser.CITY_CODES, "CUYAHOGA COUNTY", "OH", 
          "CALL UNIT ADDR CITY? INFO! INFO/N+");
    setupCities(OHCuyahogaCountyParser.CITY_LIST);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      
      // There should be a city.  If it wasn't found in it's own field
      // see if it can be found at the begining of the info field
      if (data.strCity.length() == 0) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field, data);
        if (data.strCity.length() == 0) abort();
        field = getLeft();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CITY INFO";
    }
  }
}
