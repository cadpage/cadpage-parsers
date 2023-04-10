package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class GAMurrayCountyBParser extends DispatchH05Parser {

  public GAMurrayCountyBParser() {
    super("MURRAY COUNTY", "GA",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY/S6! ID:ID! PRI:PRI! DATE:DATETIME! UNIT:UNIT! INFO:EMPTY! INFO_BLK/N+");
  }

  @Override
  public String getFilter() {
    return "newworldadmin@murraycountyga.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strPlace)) data.strPlace = "";
      super.parse(field, data);
      data.strCity = stripFieldEnd(data.strCity, data.strApt);
    }
  }

}
