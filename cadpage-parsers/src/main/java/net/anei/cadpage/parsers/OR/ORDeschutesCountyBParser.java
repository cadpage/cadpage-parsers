package net.anei.cadpage.parsers.OR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORDeschutesCountyBParser extends HtmlProgramParser {

  public ORDeschutesCountyBParser() {
    super("DESCHUTES COUNTY", "OR",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY/S6! CROSS_ST:X! ID:ID% DATE:DATETIME% UNIT:UNIT% INFO:INFO% INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cad@dc911sd.org,@deschutes.org,7017710262";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    if (!subject.equals("!") && !subject.startsWith("Automatic R&R Notification:")) return false;

    if (body.startsWith("<")) {
      return super.parseHtmlMsg(subject, body, data);
    } else {
      return parseFields(body.split("\n"), data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CITY_PTN = Pattern.compile("[A-Z][a-z][A-Za-z ]+");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (!CITY_PTN.matcher(city).matches()) {
        apt = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      field = p.get().replace('@',  '/');
      if (!apt.isEmpty()) field = stripFieldEnd(field, ' '+apt);
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      if (!apt.isEmpty() && !apt.equalsIgnoreCase(data.strApt)) data.strApt = append(data.strApt, "-", apt);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {

      // Forgive a badly formatted date/time field if it is the last field and probably truncated
      if (DATE_TIME_PTN.matcher(field).matches()) {
        super.parse(field, data);
      } else {
        if (!isLastField()) abort();
      }
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Radio Channel -")) {
        data.strChannel = append(data.strChannel, "/", field.substring(15).trim());
      } else {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }

}
