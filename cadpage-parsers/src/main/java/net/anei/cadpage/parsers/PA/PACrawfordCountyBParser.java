package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PACrawfordCountyBParser extends FieldProgramParser {
  
  public PACrawfordCountyBParser() {
    super("CRAWFORD COUNTY", "PA", 
          "Inc_Code:CALL! Address:ADDRCITY! Common_Name:PLACE! Name:NAME Units:UNIT Cross_Streets:X! Grid:MAP END");
  }
  
  @Override
  public String getFilter() {
    return "alerts@crawfordcounty.ealertgov.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "20 ");
      super.parse(field, data);
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "20 ");
      super.parse(field, data);
    }
  }
}
