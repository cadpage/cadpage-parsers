package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class KYKentonCountyBParser extends DispatchH05Parser {

  public KYKentonCountyBParser() {
    super("KENTON COUNTY", "KY",
          "CALL:CALL! PLACE:PLACE! ADDRCITYST X GPS! INFO:EMPTY! INFO_BLK/N+ Alerts:ALERT! DATE:DATETIME! INC_#'s:ID! UNITS:UNIT! TIMES:EMPTY! TIMES+");
  }

  @Override
  public String getFilter() {
    return "KCECCService@kentoncounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("X")) return new CrossField("(?:CROSS\\b *)?(.*)", true);
    if (name.equals("GPS")) return new GPSField("LAT:.* LON:.*", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strChannel = p.getLastOptional("RADIO CHANNEL:");
      data.strCall = p.get();
    }

    @Override
    public String getFieldNames() {
      return "CALL CH";
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("ADDR ")) abort();
      field = field.substring(5).trim();
      int pt = field.lastIndexOf(',');
      if (pt < 0) abort();
      String apt = field.substring(pt+1).trim();
      field = field.substring(0,pt).trim();
      super.parse(field, data);
      if (!apt.isEmpty()) {
        data.strAddress = stripFieldEnd(data.strAddress, ' '+apt);
        data.strApt = apt;
      }
    }
  }

}
