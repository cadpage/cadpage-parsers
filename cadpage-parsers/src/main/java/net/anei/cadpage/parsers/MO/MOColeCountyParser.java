package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Cole County, MO
 */
public class MOColeCountyParser extends FieldProgramParser {

  public MOColeCountyParser() {
    super("COLE COUNTY", "MO",
      "UNIT:UNIT! ADDR:ADDRCITY/S6 LOCATION_DETAILS:INFO! CROSS:X! CALL:CALL! P36:CALL/SDS! CFS_#:ID! RADIO_CH:CH! ZONE:MAP! SUMMARY:INFO/N! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "paging@jeffcitymo.org,paging@jeffersoncitymo.gov";
  }
  protected boolean parseMsg(String body, Data data) {
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("@", "&");
      super.parse(field, data);
    }
  }
}
