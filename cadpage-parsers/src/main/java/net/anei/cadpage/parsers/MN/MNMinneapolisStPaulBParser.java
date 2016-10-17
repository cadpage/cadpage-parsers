package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 Minneapolis/St Paul, MN (B)
 **/
public class MNMinneapolisStPaulBParser extends DispatchProQAParser {  
  public MNMinneapolisStPaulBParser() {
    super(CITY_LIST, "MINNEAPOLIS", "MN", 
        "PRI ID! CODE CALL ADDR ( PLACE APT/Z CITY | PLACE APT/Z PLACE2 CITY | APT CITY | PLACE CITY/Y | CITY/Y | EMPTY EMPTY EMPTY CITY/Y? ) TIME! INFO+",
        true);
  }
  
  @Override
  public String getLocName() {
    return "MPLS/St. Paul, MN";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replaceAll(" {2,}", " ");
    return super.parseMsg(body, data);
  }
  
  @Override public Field getField(String name) {
    if (name.equals("PLACE2")) return new Place2Field();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("TIME")) return new TimeField("\\d{2}:\\d{2}", true);
    return super.getField(name);
  }
    
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      field = field.trim();
      if (field.length() < 7)
        data.strApt = field;
      else
        data.strPlace = append(data.strPlace, "/", field);
    }
    
    @Override
    public String getFieldNames() {
      return append(super.getFieldNames(), " ", "PLACE");
    }
  }
  
  private class Place2Field extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(data.strPlace, "/", field.trim());
    }
  }
  
  private static final String[] CITY_LIST = {
    "BURNSVILLE",
    "COTTAGE GROVE",
    "EAGAN",
    "INVER GROVE HEIGHTS",
    "LILYDALE",
    "MAPLEWOOD",
    "MENDOTA HEIGHTS",
    "MINNEAPOLIS",
    "ROSEMOUNT",
    "SOUTH ST PAUL",
    "SOUTH ST. PAUL",
    "ST PAUL",
    "ST. PAUL",
    "WEST ST PAUL",
    "WEST ST. PAUL",
  };
}
