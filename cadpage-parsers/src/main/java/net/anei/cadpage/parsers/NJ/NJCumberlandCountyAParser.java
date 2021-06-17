package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


/**
 * Cumberland County, NJ
 */
public class NJCumberlandCountyAParser extends FieldProgramParser {


  public NJCumberlandCountyAParser() {
    super("CUMBERLAND COUNTY", "NJ",
           "UNIT CALL ADDR DATETIME! PLACE");
  }

  @Override
  public String getFilter() {
    return "E911@co.cumberland.nj.us";
  }

  private static final Pattern GEN_ALERT_PTN = Pattern.compile("CUMBERLAND911:(\\S+): *(.*)");
  private static final Pattern MASTER1 = Pattern.compile("(\\d+):([^:]+):([^:]+):([^:]+)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    Matcher match = GEN_ALERT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strUnit = match.group(1);
      data.strSupp = match.group(2);
      return true;
    }

    match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT CALL PLACE ADDR APT INFO");
      data.strCallId = match.group(1);
      data.strUnit = match.group(2).trim().toUpperCase().replace(' ', '_');
      data.strCall = match.group(3).trim();
      String address = match.group(4).trim();
      String[] parts = address.split("/");
      if (parts.length == 3) {
        data.strPlace = parts[0].trim();
        address = parts[1].trim();
        String info = parts[2].trim();
        if (!info.equals(data.strPlace)) data.strSupp = info;
      }
      parseAddress(address, data);
      return true;
    }

    if (body.startsWith("E911:")) body = body.substring(5).trim();
    if (subject.length() > 0 && !subject.equals("Text Message")) body = subject + "_" + body;
    if (!parseFields(body.split("_"), data)) return false;
    if (data.strUnit.endsWith("AC")) {
      data.strCall = "ALL CALL - " + data.strCall;
    }
    return true;
  }

  private static final Pattern ADDR_PTN = Pattern.compile("^\\((.*)\\)");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {

      // City is in front in parenthesis
      Matcher match = ADDR_PTN.matcher(field);
      if (match.find()) {
        data.strCity = stripFieldEnd(match.group(1), " BORO");
        field = field.substring(match.end()).trim();
      }

      // slash divides address into two parts, either which can be a place name
      // or they can both be streets

      int pt = field.indexOf('/');
      if (pt >= 0) {
        String fld1 = field.substring(0,pt).trim();
        String fld2 = field.substring(pt+1).trim();

        if (!isValidAddress(fld1)) {
          data.strPlace = fld1;
          field = fld2;
        } else if (!isValidAddress(fld2)) {
          data.strPlace = fld2;
          field = fld1;
        }
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CITY ADDR APT PLACE";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(?:(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d) )?(\\d\\d:\\d\\d(?::\\d\\d)?)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      String year = match.group(2);
      if (year != null) data.strDate = year + "/" + match.group(3) + "/" + match.group(1);
      data.strTime = match.group(4);
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(data.strPlace, " / ", field);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }
}
