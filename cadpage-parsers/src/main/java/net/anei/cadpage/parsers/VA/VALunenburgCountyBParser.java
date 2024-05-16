package net.anei.cadpage.parsers.VA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VALunenburgCountyBParser extends FieldProgramParser {

  public VALunenburgCountyBParser() {
    super("LUNENBURG COUNTY", "VA",
          "UNIT CALL ADDRCITY ID DATETIME! X NOTES:INFO END");
  }

  @Override
  public String getFilter() {
    return "LunenburgCAD@lunenburg911.com,cad@lunenburg911.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(" \\| "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+", true);
    if (name.equals("ADDRCITY"))  return new MyAddressCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("(.*), ([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strState = match.group(2);
        zip = match.group(3);
      }
      super.parse(field, data);
      if (zip != null && data.strCity.length() == 0) data.strCity = zip;
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }
}
