package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILKaneCountyDParser extends HtmlProgramParser {

  public ILKaneCountyDParser() {
    super("KANE COUNTY", "IL",
          "EMPTY+? DATETIME UNIT CODE_CALL ADDRCITY/S6 X/Z MAP GPS1 GPS2 ID1 ID2/L! INFO/N+");
    setPreserveWhitespace(true);
  }

  @Override
  public String getFilter() {
    return "@quadcom911.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification: ")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MapField("[A-Z]{2}\\d{4}[A-Z]?|[A-Z][-A-Z]+\\d|ELGIN.*|", true);
    if (name.equals("ID1")) return new IdField("\\d+", true);
    if (name.equals("ID2")) return new MyIdField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([-A-Z0-9]{1,6})[- ]+(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strCall = match.group(2);
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{4,}|-361");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("[Incident");
      if (pt >= 0) {
        field = field.substring(0,pt).trim();
        field = stripFieldEnd(field, ",");
      }
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_DATE_PTN = Pattern.compile("\\*{3}(\\d\\d?/\\d\\d?/\\d{4})\\*{3}");
  private static final Pattern INFO_TIME_PTN = Pattern.compile("\\d\\d?:\\d\\d:\\d\\d");
  private static final Pattern INFO_PFX_PTN = Pattern.compile("QUADCOM[\\\\\\s]+[A-Za-z0-9]+ - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_DATE_PTN.matcher(field);
      if (match.matches()) {
        data.strDate = match.group(1);
        return;
      }

      if (INFO_TIME_PTN.matcher(field).matches()) {
        data.strTime = field;
        return;
      }

      match = INFO_PFX_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
}
