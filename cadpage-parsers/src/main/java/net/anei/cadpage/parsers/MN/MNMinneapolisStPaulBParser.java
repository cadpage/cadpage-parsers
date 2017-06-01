package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 Minneapolis/St Paul, MN (B)
 **/
public class MNMinneapolisStPaulBParser extends DispatchProQAParser {  
  public MNMinneapolisStPaulBParser() {
    super(CITY_LIST, "MINNEAPOLIS", "MN", 
        "PRI ID! CALL CALL/SDS ADDR PLACE APT+? CITY INFO+? TIME! INFO+",
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
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("TIME")) return new TimeField("\\d{2}:\\d{2}", true);
    return super.getField(name);
  }
    
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      field = field.trim();
      if (field.length() < 7)
        data.strApt = append(data.strApt, "-", field);
      else
        data.strPlace = append(data.strPlace, "/", field);
    }
    
    @Override
    public String getFieldNames() {
      return append(super.getFieldNames(), " ", "PLACE");
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      
      // An empty field can be classified as a city, but only if the
      // first non-empty field behind it is not a city
      if (field.length() == 0) {
        for (int ndx = 1; ; ndx++) {
          if (isLastField(ndx)) return false;
          String fld = getRelativeField(ndx);
          if (fld.length() > 0) return !isCity(fld);
        }
      } 
      
      // Otherwise use usual city logic
      else {
        return super.checkParse(field, data);
      }
    }
  }
  
  private static final String[] CITY_LIST = {
      "APPLE VALLEY",
      "BURNSVILLE",
      "COTTAGE GROVE",
      "EAGAN",
      "EDINA",
      "INVER GROVE",
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
      "WOODBURY"
  };
}
