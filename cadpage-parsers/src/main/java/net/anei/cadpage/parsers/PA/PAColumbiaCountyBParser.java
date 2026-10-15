package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PAColumbiaCountyBParser extends DispatchH05Parser {

  public PAColumbiaCountyBParser() {
    super("COLUMBIA COUNTY", "PA",
          "CALL ADDRCITY PLACE? UNIT DATETIME ID GPS! INFO_BLK+? TIMES");
  }

  @Override
  public String getFilter() {
    return "cad@csr911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
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
      super.parse(field,  data);
      data.strCity = stripFieldEnd(data.strCity, "BORO");
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = stripFieldStart(addr, "AREA OF ");
    return addr;
  }
}
