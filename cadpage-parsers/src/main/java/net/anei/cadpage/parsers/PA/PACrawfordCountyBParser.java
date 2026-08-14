package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PACrawfordCountyBParser extends DispatchH05Parser {

  public PACrawfordCountyBParser() {
    super("CRAWFORD COUNTY", "PA",
          "Date_Time:DATETIME! Inc_Code:CALL! Address:ADDRCITY! Common_Name:PLACE! Name:NAME! Cross_Streets:X! Grid:MAP Maps:EMPTY! SKIP+? GPS! " +
              "( Narrative:EMPTY! INFO_BLK+ | ) Phone:PHONE! Alert_Code:CODE! END");
  }

  @Override
  public String getFilter() {
    return "noreply@ntr911sa.com";
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
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("GPS")) return new GPSField("Lat:.* Long:.*", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
      data.strCity = stripFieldEnd(data.strCity, " BORO");
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
