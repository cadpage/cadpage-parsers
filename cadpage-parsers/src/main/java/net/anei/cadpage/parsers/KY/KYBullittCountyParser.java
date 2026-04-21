package net.anei.cadpage.parsers.KY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class KYBullittCountyParser extends FieldProgramParser {

  public KYBullittCountyParser() {
    super("BULLITT COUNTY", "KY",
          "( UNIT ID | ID ) CALL ADDRCITYST NAME PHONE ( EMPTY! | DATETIME! ) INFO/N+");
  }

  public String getFilter() {
    return "911@bullittky.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[#$]([A-Z]{2,6}\\d{2}-\\d{6})|\\d{8}|", true);
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern BAD_ST_ZIP_PTN = Pattern.compile("\\b([A-Z]{2}), *(\\d{5})$");

  private class MyAddressCityStateField extends AddressCityStateField {

    @Override
    public void parse(String field, Data data) {
      field = BAD_ST_ZIP_PTN.matcher(field).replaceAll("$1 $2");
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
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
