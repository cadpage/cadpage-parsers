package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHFairfieldCountyBParser extends DispatchH05Parser {

  public OHFairfieldCountyBParser() {
    super("FAIRFIELD COUNTY", "OH",
          "Date/Time:DATETIME! Location:ADDR_CITY_PLACE! Call_Type:CALL! Nature_of_Call:CALL/SDS! Radio_Channel:CH! Incident#:ID! " +
                "Times:EMPTY! TIMES+ Narrative:EMPTY! INFO_BLK+ https:GPS! END");
  }

  @Override
  public String getFilter() {
    return "fcsocadnoreply@fairfieldcountyohio.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDR_CITY_PLACE")) return new MyAddressCityPlaceField();
    if (name.equals("GPS")) return new GPSField(".*&query=(.*)", true);
    return super.getField(name);
  }

  private class MyAddressCityPlaceField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(",Common Name:");
      if (pt < 0) abort();
      data.strPlace = field.substring(pt+13).trim();
      field = field.substring(0, pt).trim();
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
}
