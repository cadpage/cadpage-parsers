package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHFranklinCountyBParser extends DispatchH05Parser {

  public OHFranklinCountyBParser() {
    super("FRANKLIN COUNTY", "OH",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! DATE:DATETIME! RADIO:CH! Units:UNIT! INFO:EMPTY! INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "nwps@grovecityohio.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("([^,]*,[^,]*), *([^,]*)");

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }
      super.parse(field, data);
      if (!apt.isEmpty()) {
        data.strAddress = stripFieldEnd(data.strAddress, ' '+apt);
        data.strApt = append(data.strApt, "-", apt);
      }
    }
  }
}
