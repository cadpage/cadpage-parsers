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
           "UNIT CALL ADDR DATETIME! UNIT_PLACE");
  }

  @Override
  public String getFilter() {
    return "E911@co.cumberland.nj.us,Cumberland911@co.cumberland.nj.us";
  }

  private static final Pattern GEN_ALERT_PTN1 = Pattern.compile("(\\S+): *(.*)");
  private static final Pattern GEN_ALERT_PTN2 = Pattern.compile("((?:\\b(?:[A-Z]\\d+|\\d+[A-Z])\\b ??))[- ]*(.*)");
  private static final Pattern MASTER1 = Pattern.compile("(\\d+):(?:Text Message)?([^:]+):([^:]+):([^:]+)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (body.startsWith("CUMBERLAND911:") || body.startsWith("Cumberland911:")) {
      setFieldList("UNIT INFO");
      data.msgType = MsgType.GEN_ALERT;
      body = stripFieldStart(body.substring(15), "Text Message");
      Matcher match = GEN_ALERT_PTN1.matcher(body);
      if (match.matches()) {
        data.strUnit = match.group(1);
        data.strSupp = match.group(2);
      } else if (( match = GEN_ALERT_PTN2.matcher(body)).matches()) {
        data.strUnit = match.group(1);
        data.strSupp = match.group(2);
      } else {
        data.strSupp = body;
      }
      return true;
    }

    Matcher match = MASTER1.matcher(body);
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

    body = stripFieldStart(body, "E911:");
    body = stripFieldStart(body, "Text Message");
    body = stripFieldStart(body, "E911:");
    if (subject.length() > 0 && !subject.equals("Text Message")) body = subject + "_" + body;
    if (!parseFields(body.split("_"), data)) return false;
    if (data.strUnit.endsWith("AC")) {
      data.strCall = "ALL CALL - " + data.strCall;
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("UNIT_PLACE")) return new MyUnitPlaceField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {

      // City is in front in parenthesis
      if (field.startsWith("(")) {
        int pt = field.lastIndexOf(')');
        if (pt >= 0) {
          String city = field.substring(1, pt).trim();
          field = field.substring(pt+1).trim();
          pt = city.indexOf(')');
          if (pt >= 0) {
            data.strPlace = city.substring(pt+1).trim() + ')';
            city = city.substring(0,pt).trim();
          }
          pt = city.indexOf('(');
          if (pt >= 0) city = city.substring(0,pt).trim();
          city = stripFieldEnd(city, " BORO");
          data.strCity = city;
        }
      }

      // slash divides address into two parts, either which can be a place name
      // or they can both be streets

      String place = "";
      int pt = field.indexOf('/');
      if (pt >= 0) {
        String fld1 = field.substring(0,pt).trim();
        String fld2 = field.substring(pt+1).trim();

        if (!isValidAddress(fld1)) {
          place = fld1;
          field = fld2;
        } else if (!isValidAddress(fld2)) {
          place = fld2;
          field = fld1;
        }
      }

      data.strPlace = append(data.strPlace, " - ", place);
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

  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]*\\d+[A-Z]?");
  private class MyUnitPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      if (field.equals(data.strUnit)) return;
      StringBuilder sb = new StringBuilder(data.strUnit);
      for (String unit : field.split("[ ,]+")) {
        if (!UNIT_PTN.matcher(unit).matches()) {
          data.strPlace = append(data.strPlace, " - ", field);
          return;
        }
        if (!unit.equals(data.strUnit)) {
          if (sb.length() > 0) sb.append(',');
          sb.append(unit);
        }
      }
      data.strUnit = sb.toString();
    }

    @Override
    public String getFieldNames() {
      return "UNIT PLACE";
    }
  }
}
