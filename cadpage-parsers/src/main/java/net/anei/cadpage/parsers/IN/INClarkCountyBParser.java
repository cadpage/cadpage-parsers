package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class INClarkCountyBParser extends DispatchH05Parser {

  public INClarkCountyBParser() {
    super("CLARK COUNTY", "IN",
          "DATETIME MAP ADDRCITY ( CALL ST_INFO_BLK INFO_BLK/Z+? UNIT! " +
                                "| CALL UNIT! " +
                                "| X CALL INFO_BLK/Z+? UNIT! " +
                                "| PLACE CALL ST_INFO_BLK INFO_BLK/Z+? UNIT! " +
                                "| PLACE CALL UNIT! " +
                                "| PLACE X CALL INFO_BLK/Z+? UNIT! " +
                                ") TIMES+");


  }

  @Override
  public String getFilter() {
    return "alert@clarkcounty911.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z]+\\d+|[A-Z]+FD|[A-Z]+EMS|CALL [ A-Z]+)\\b[, ]*)+", true);
    return super.getField(name);
  }

  private static final Pattern TRAIL_APT_PTN = Pattern.compile("(.*,.*?) (\\.?\\d+[- ]?[A-Z]?|[A-Z]+)");

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = null;
      Matcher match = TRAIL_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        apt = match.group(2);
      }
      super.parse(field, data);
      if (apt != null) {
        data.strAddress = stripFieldEnd(data.strAddress, ' '+apt);
        data.strApt = append(data.strApt, "-", apt);
      }
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(" / ")) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
