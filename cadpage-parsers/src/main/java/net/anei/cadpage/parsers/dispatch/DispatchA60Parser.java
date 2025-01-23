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
          "CODE/Z? CALL ADDRCITYST/Z INFO INFO+? UNIT/C+? DATETIME! ID END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.endsWith(":")) body += ' ';
    if (!parseFields(body.split(": "), data)) return false;
    if (data.strCallId.equals("None")) data.strCallId = "";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new CodeField("\\*?[A-Z][-A-Z0-9]+|", false);
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new MyDateTimeField("\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "=");
      super.parse(field, data);
    }
  }

  private static final Pattern LOG_PATTERN
    = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) -(?: LOG -)? *", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("None")) return true;
      return parseEntries(field.split("; *"), data);
    }

    private boolean parseEntries(String[]flds, Data data) {
      String date = null;
      String time = null;
      StringBuilder sb = new StringBuilder();
      boolean good = false;

      for (String field : flds) {
        field = field.trim();
        if (field.isEmpty()) continue;
        Matcher m = LOG_PATTERN.matcher(field);
        if (m.lookingAt()) {
          good = true;
          date = m.group(1);
          time =  m.group(2);
          field = field.substring(m.end());
          if (field.isEmpty()) continue;
        }
        if (sb.length() > 0) sb.append('\n');
        sb.append(field);
      }
      if (!good) return false;
      data.strDate = date;
      data.strTime = time;
      data.strSupp = append(data.strSupp, "\n", sb.toString());
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
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
