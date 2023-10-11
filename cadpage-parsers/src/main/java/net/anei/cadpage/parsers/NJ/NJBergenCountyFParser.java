package net.anei.cadpage.parsers.NJ;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJBergenCountyFParser extends FieldProgramParser {

  public NJBergenCountyFParser() {
    super("BERGEN COUNTY", "NJ",
          "CAD_No:ID! Time_of_Call:DATETIME! Location:ADDRCITY! Incident_Type:CALL! Notes:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "donotreply@lawsoftweb.onmicrosoft.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("EMS::Time of Call:", "Time of Call:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d(?::\\d\\d)? [AP]M)");
  private static final SimpleDateFormat TIME_SEC_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static final SimpleDateFormat TIME_NOSEC_FMT = new SimpleDateFormat("hh:mm aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      SimpleDateFormat fmt = time.length() > 8 ? TIME_SEC_FMT : TIME_NOSEC_FMT;
      setTime(fmt, time, data);
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strCity = stripFieldEnd(data.strCity, " Boro");
    }
  }
}
