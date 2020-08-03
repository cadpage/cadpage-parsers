package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Floyd County, GA
 */
public class DispatchA60Parser extends FieldProgramParser {

  public DispatchA60Parser(String defCity, String defState) {
    super(defCity, defState,
          "CODE? CALL ADDR/Z INFO UNIT DATETIME! END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(": "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new MyDateTimeField("\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern CITY_PATTERN = Pattern.compile("(.*), *([A-Z][ A-Z]{2,})(?:, *([A-Z]{2})(?: +\\d{5})?)?");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;

      // Strip off leading '='
      field = stripFieldStart(field, "=");
      Matcher m = CITY_PATTERN.matcher(field);
      if (m.matches()) {
        field = m.group(1).trim();
        data.strCity = m.group(2).trim();
        data.strState = getOptGroup(m.group(3));
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" CITY ST";
    }
  }

  private static final Pattern LOG_PATTERN
    = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) -(?: LOG -)? *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (!field.equals("None"))
        parseEntries(field.split("; *"), data);
    }

    private void parseEntries(String[]f, Data data) {
      for (String part : f) parseEntry(part.trim(), data);
    }

    private void parseEntry(String field, Data data) {
      Matcher m = LOG_PATTERN.matcher(field);
      if (m.lookingAt()) {
        data.strDate = m.group(1);
        data.strTime =  m.group(2);
        field = field.substring(m.end());
      }
      data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace("; ", ","), data);
    }
  }

  private class MyDateTimeField extends DateTimeField {

    public MyDateTimeField(String p, boolean h) {
      super(p, h);
    }

    @Override
    public void parse(String field, Data data) {
      if (data.strDate.length() > 0) return;
      super.parse(field, data);
    }
  }
}
