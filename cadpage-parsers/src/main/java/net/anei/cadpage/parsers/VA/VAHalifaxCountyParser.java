package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;

public class VAHalifaxCountyParser extends DispatchA47Parser {
  
  public VAHalifaxCountyParser() {
    super("from Central", CITY_LIST, "HALIFAX COUNTY", "VA", null);
  }
  
  @Override
  public String getFilter() {
    return "halifaxeoc@co.halifax.va.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("\n\n", "\n");
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.toUpperCase().startsWith("TURBEVILLE")) data.strCity = data.strCity.substring(0,10);
    if (data.strPlace.startsWith("911")) {
      data.strSupp = append(data.strPlace, "\n", data.strSupp);
      data.strPlace = "";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }
  
  private class MyPlaceField extends PlaceField {
    
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() == 0) {
        Parser p = new Parser(field);
        data.strCity = p.getLast("  ");
        field = p.get();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE CITY";
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Towns
    "HALIFAX",
    "SCOTTSBURG",
    "SOUTH BOSTON",
    "VIRGILINA",

    // Census-designated places
    "CLOVER",
    "CLUSTER SPRINGS",
    "MOUNTAIN ROAD",
    "NATHALIE",
    "RIVERDALE",

    // Other unincorporated communities
    "ALTON",
    "CODY",
    "TURBEVILLE",
    "VERNON HILL"
  };

}