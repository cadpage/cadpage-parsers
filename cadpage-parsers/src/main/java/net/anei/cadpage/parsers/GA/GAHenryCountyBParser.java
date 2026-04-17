package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class GAHenryCountyBParser extends DispatchH05Parser {

  public GAHenryCountyBParser() {
    super("HENRY COUNTY", "GA",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:EMPTY INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "@co.henry.ga.us";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*), *([A-Z]?\\d+[A-Z]?)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = null;
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      } else {
        field = stripFieldEnd(field, ",");
      }
      super.parse(field, data);
      if (apt != null) {
        if (data.strApt.isEmpty()) {
          data.strAddress = stripFieldEnd(data.strAddress, ' '+apt);
        }
        data.strApt = append(data.strApt, "-", apt);
      }
    }
  }
}
