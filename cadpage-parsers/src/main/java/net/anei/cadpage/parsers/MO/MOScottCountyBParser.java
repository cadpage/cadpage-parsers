package net.anei.cadpage.parsers.MO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOScottCountyBParser extends FieldProgramParser {

  public MOScottCountyBParser() {
    super("SCOTT COUNTY", "MO",
          "ID DATETIME! Situation:CALL! Address:ADDRCITY! CALL/SDS Notes:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "SIKESTONDPS@OMNIGO.COM";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d\\d-\\d{5}", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
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

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf("   ");
      if (pt >= 0) {
        data.strCity = field.substring(pt+3);
        field = field.substring(0,pt).trim();
      }
      parseAddress(field, data);
    }
  }
}
