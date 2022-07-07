package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MSHarrisonCountyFParser extends FieldProgramParser {
  
  public MSHarrisonCountyFParser() {
    super(MSHarrisonCountyParser.CITY_LIST, "HARRISON COUNTY", "MS", 
          "DATETIME ADDR ID UNIT! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "FDALERTS@GULFPORT-MS.GOV";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCall = p.get('@');
      String addr = p.get(',');
      if (addr.isEmpty()) abort();
      parseAddress(StartType.START_ADDR, FLAG_NO_CITY | FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
      String city = p.get();
      if (!city.isEmpty()) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
        if (data.strCity.isEmpty()) {
          data.strCity = getLeft();
        } else {
          data.strPlace = getLeft();
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CALL ADDR APT CITY PLACE";
    }
  }
}
