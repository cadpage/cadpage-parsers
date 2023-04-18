package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class KYMcCrackenCountyParser extends DispatchH05Parser {

  public KYMcCrackenCountyParser() {
    super("MCCRACKEN COUNTY", "KY",
          "CALL_DATE/TIME:DATETIME! CALL_TYPE:CALL! LOCATION:ADDRCITY! XST:X! CMTS:EMPTY! INFO_BLK+ UNITS:UNIT! INC#:ID! CALL_TIMES:EMPTY! TIMES+? GPS! END");
  }

  @Override
  public String getFilter() {
    return "cad@paducahky.gov";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("LATITUDE: *(\\S*) +LONGITUDE: *(\\S*)");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      setGPSLoc(match.group(1)+','+match.group(2), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
