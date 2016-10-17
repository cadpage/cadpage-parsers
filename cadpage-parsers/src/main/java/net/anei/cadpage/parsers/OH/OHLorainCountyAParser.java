package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class OHLorainCountyAParser extends DispatchOSSIParser {

  public OHLorainCountyAParser() {
    super(CITY_CODES, "LORAIN COUNTY", "OH",
           "( CANCEL | FYI CALL ) ADDR/S! APT? DIST? ( SELECT_CITY | PLACE+? CITY ) PLACE+");
    setupCities(CITY_LIST);
  }
  
  @Override
  public String getFilter() {
    return "cad@lorainco911.com,CAD@windstream.net";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("CAD:") &&
        (subject.equals("FYI") || subject.equals("Update") || subject.equals("CANCEL"))) {
      body = "CAD:" +  subject + ": " + body.substring(4);
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new AptField("(?:APT|RM|LOT) *(.*)|(\\d{1,4}[A-Z]?|[A-D])");
    if (name.equals("DIST")) return new MyDistField();
    if (name.equals("SELECT_CITY")) return new MySelectCityField();
    return super.getField(name);
  }
  
  private class MyDistField extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("DIST:")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("DIST: DIST:")) field = field.substring(6);
      super.parse(field, data);
    }
  }
  
  private class MySelectCityField extends SelectField {
    @Override
    public boolean checkParse(String field, Data data) {
      return (data.strCity.length() > 0);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
     "ALK", "AVON LAKE",
     "AMH", "AMHERST",
     "ATP", "AMHERST TWP",
     "AVN", "AVON",
     "CAR", "CARLISLE TWP",
     "COL", "COLUMBIA TWP",
     "EAT", "EATON TWP",
     "ELY", "ELYRIA",
     "KIP", "KIPTON",
     "LOR", "LORAIN",
     "NEW", "NEW RUSSIA TWP",
     "NRG", "NORTH RIDGEVILLE",
     "PEN", "PENFIELD TWP",
     "SLK", "SHEFFIELD LAKE",
     "SVG", "SHEFFIELD"
  });
  
  private static String[] CITY_LIST = new String[]{
    "BAY VILLAGE",
    "VERMILION",
    "WESTLAKE"
  };
}
