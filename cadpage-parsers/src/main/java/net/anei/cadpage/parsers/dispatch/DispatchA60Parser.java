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
          "CODE/X? CALL ADDRCITYST/Z! ( DATETIME! INFO+? UNIT/C+ " +
                                     "| INFO INFO+? UNIT/C+? DATETIME! ID " +
                                     ") END");
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
//    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new MyDateTimeField("\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern CODE_PTN = Pattern.compile("\\*?[A-Z][-A-Z0-9]+|");
  private static final Pattern ADDR_PTN = Pattern.compile("\\d.*|.*[,/&].*|\\{cfs_location\\}");
  private class MyCodeField extends CodeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!CODE_PTN.matcher(field).matches()) return false;
      if (ADDR_PTN.matcher(getRelativeField(+1)).matches()) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
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
          if (data.strDate.isEmpty()) {
            data.strDate = m.group(1);
            data.strTime =  m.group(2);
          }
          field = field.substring(m.end());
          if (field.isEmpty()) continue;
        }
        if (sb.length() > 0) sb.append('\n');
        sb.append(field);
      }
      if (!good) return false;
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
