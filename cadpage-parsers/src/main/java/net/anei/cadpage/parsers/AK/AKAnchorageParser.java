package net.anei.cadpage.parsers.AK;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class AKAnchorageParser extends FieldProgramParser {

  public AKAnchorageParser() {
    super("ANCHORAGE", "AK",
          "INCIDENT:ID! TITLE:CALL! PLACE:PLACE? ADDRESS:ADDR? GPS:GPS? BOX:BOX? NOTES:SKIP!");
  }

  @Override
  public String getFilter() {
    return "jberfire907@gmail.com,monacoenterprises2014@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      int pt =  field.indexOf(" : ");
      if (pt >= 0) field = field.substring(pt+3).trim();
      super.parse(field, data);
    }
  }
}
