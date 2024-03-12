package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 * Clearfield County, PA
 */
public class PAClearfieldCountyAParser extends DispatchH05Parser {

  public PAClearfieldCountyAParser() {
    super("CLEARFIELD COUNTY", "PA",
           "Inc_Code:CALL! Address:ADDRCITY! Common_Name:PLACE! Units:UNIT! Cross_Streets:X! Alert_Code:CODE! END");
  }

  @Override
  public String getFilter() {
    return "noreply@ntr911sa.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strCity = stripFieldEnd(data.strCity, " BORO");
    }
  }
}
