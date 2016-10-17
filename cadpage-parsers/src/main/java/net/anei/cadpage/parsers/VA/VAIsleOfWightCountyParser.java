package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAIsleOfWightCountyParser extends DispatchOSSIParser {
  
  public VAIsleOfWightCountyParser() {
    super("ISLE OF WIGHT COUNTY", "VA",
           "FYI? ADDR APT2? DIST? ( X X? CALL | CALL X X? | PLACE CALL X X? | CALL ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "@isleofwightUS.net";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  // Cross street doesn't permit name ending with PLACE on
  // the theory that this is probably a place name
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.endsWith(" PLACE")) return false;
      return super.checkParse(field, data);
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(field, " - ", data.strPlace);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT2")) return new Apt2Field(3);
    if (name.equals("DIST")) return new PlaceField("DIST:.*");
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
}