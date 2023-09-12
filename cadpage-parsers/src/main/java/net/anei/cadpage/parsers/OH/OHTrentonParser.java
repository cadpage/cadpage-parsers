package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class OHTrentonParser extends FieldProgramParser {

  public OHTrentonParser() {
    super("TRENTON", "OH",
          "SRC_CALL! CAD_#:ID! Address:ADDRCITY! Units:UNIT Incident_Code:SKIP Cross_Streets:X! Call_Details:INFO! INFO/N+ Page_Time:DATETIME");
  }


  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Trenton Paging System");
    body = stripFieldStart(body, "-");
    return parseFields(body.split(" - "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC_CALL"))  return new MySourceCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern SRC_CALL_PTN = Pattern.compile("([A-Z]{3,5})\\b *(.*)");
  private class MySourceCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = SRC_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = match.group(1);
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "SRC CALL";
    }
  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city;
      String zip = null;
      while (true) {
        city = p.getLastOptional(',');
        Matcher match = ST_ZIP_PTN.matcher(city);
        if (!match.matches()) break;
        data.strState = match.group(1);
        String t = match.group(2);
        if (t != null) zip = t;
      }
      if (city.isEmpty() && zip != null) city = zip;
      data.strCity = city;
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_DATE_TIME_PTN.matcher(field).replaceAll("");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\((\\d\\d?/\\d\\d?/\\d\\d(?:\\d\\d)?) +(\\d\\d?:\\d\\d [AP]M)\\)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
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
