package net.anei.cadpage.parsers.RI;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class RIWashingtonCountyParser extends FieldProgramParser {
 
  public RIWashingtonCountyParser() {
    super(CITY_LIST, "WASHINGTON COUNTY", "RI",
           "URL? DATETIME ADDR/S6 PLACE? CITY CALL CALL ( ASGN_UNIT UNIT EMPTY | ) NOTES INFO");
    setupSpecialStreets("COLUMBIA HTS OVAL");
  }
  
  @Override
  public String getFilter() {
    return "cad_do_not_reply@westerlyambulance.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Notification")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() != 19) abort();
      data.strDate = field.substring(5,10) + "/" + field.substring(0,4);
      data.strTime = field.substring(11);
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace('@', 'I'), data);
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NO NAME")) return;
      field = field.replace('@', 'I');
      super.parse(field, data);
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ASGN_UNIT")) return new SkipField("Assigned Units", true);
    if (name.equals("NOTES")) return new SkipField("Notes", true);
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "CHARLESTOWN",
    "CAROLINA",
    "EXETER",
    "HOPKINTON",
    "ASHAWAY",
    "HOPE VALLEY",
    "NARRAGANSETT",
    "GALILEE",
    "NARRAGANSETT PIER",
    "NEW SHOREHAM",
    "NORTH KINGSTOWN",
    "WICKFORD",
    "SAUNDERSTOWN",
    "RICHMOND",
    "CAROLINA",
    "KENYON",
    "SHANNOCK",
    "USQUEPAUG",
    "WYOMING",
    "SOUTH KINGSTOWN",
    "KINGSTON",
    "MATUNUCK",
    "PEACEDALE",
    "WAKEFIELD",
    "WESTERLY",
    "BRADFORD",
    "WATCH HILL",
    "WHITE ROCK"
  };
}
