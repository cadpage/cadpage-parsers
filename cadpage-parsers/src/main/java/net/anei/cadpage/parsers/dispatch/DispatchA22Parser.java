package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/*
 * Klamath County, OR
 * Similar to, possible merge with Crook County, OR
 */

abstract public class DispatchA22Parser extends FieldProgramParser {

  public DispatchA22Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA22Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "EVENT_PAGE? DATETIME! UNITS:UNIT? IDSRC! CALL/SDS! ( PRIORITY:PRI | PRI:PRI | ) ( LOCATION:ADDR! CITY:CITY APT:APT PREMISE:PLACE? | ADDR ( CITY:CITY! | CITY! ) APT:APT? ) INFO+");
  }

  private static Pattern KEYWORD_PTN1 = Pattern.compile("(?<=\n(?:UNITS|EVENT #|PRIORITY|PRI|LOCATION|CITY|APT|PREMISE|COMMENT))(?![:A-Z])");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "INCIDENT NEW\n");
    body = stripFieldStart(body, "UNIT DISPATCH\n");
    body = stripFieldEnd(body, ",");
    body = stripFieldEnd(body, "...");
    body = stripFieldEnd(body, ",");

    // The standard format has line breaks between each field.  On some occasions these are replaced with
    // blanks and have to be manually restored.  Which requires the use of a source pattern to identify
    // the the missing line break following the source field :(
      body = KEYWORD_PTN1.matcher(body).replaceAll(":");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("EVENT_PAGE")) return new CallField("(.*?) *-EVENT PAGE");
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("IDSRC")) return new MyIdSourceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(?:(.*?)-)?(\\d{1,2}/\\d{1,2}/\\d{4}) (\\d{4})");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = append(data.strCall, " - ", getOptGroup(match.group(1)));
      data.strDate = match.group(2);
      String time = match.group(3);
      data.strTime = time.substring(0,2) + ':' + time.substring(2,4);
    }
  }

  private static final Pattern ID_SRC_PTN = Pattern.compile("(\\d{10})(?: +(.*))?");
  private class MyIdSourceField extends Field {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "EVENT #:");
      Matcher match = ID_SRC_PTN.matcher(field);
      if (match.matches()) {
        data.strCallId = match.group(1).trim();
        data.strSource =  getOptGroup(match.group(2));
      } else {
        data.strSource = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "ID SRC";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE?";
    }
  }

  private static final Pattern COMMENT_PTN = Pattern.compile("^COMMENT[: ] *");
  private static final Pattern INFO_GPS_PTN = Pattern.compile("[-+]\\d{2,3}\\.\\d{6}[, ]+[-+]\\d{2,3}\\.\\d{6}");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = COMMENT_PTN.matcher(field).replaceFirst("");
      if ("COMMENT".startsWith(field)) return;
      Matcher match= INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(field, data);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS";
    }
  }
}