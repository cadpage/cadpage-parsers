package net.anei.cadpage.parsers.MT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTLewisAndClarkCountyCParser extends FieldProgramParser {

  public MTLewisAndClarkCountyCParser() {
    super("LEWIS AND CLARK COUNTY", "MT",
          "ID ( CODE ADDRCITYST PLACE GPS1 GPS2 CODE/L CALL CODE/L CALL/L NAME PHONE DATETIME INFO EMPTY " +
             "| ADDRCITYST/Z INFO_CODE CALL/L CODE/L CALL/L NONE_DATETIME INFO " +
             "| CODE_ADDRCITYST CODE CALL/L CALL/L NONE_DATETIME INFO " +
             ") UNIT EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "dispatch@helenamt.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    body = body.replace("|INFORMATION PAGE   ", "|INFORMATION PAGE|");
    return parseFields(body.split("\\|",-1), data);
  }

  @Override
  public boolean parseFields(String[] fields, Data data) {
    for (int ndx = 0; ndx < fields.length; ndx++) {
      if (fields[ndx].trim().equals("None")) fields[ndx] = "";
    }
    return super.parseFields(fields, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{6}-\\d{3}");
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("INFO_CODE")) return new CodeField("INFO", true);
    if (name.equals("CODE_ADDRCITYST")) return new MyCodeAddressCityStateField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("NONE_DATETIME")) return new DateTimeField("(?:None|\\**INFORMATION PAGE\\**) +(\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d)", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final String CODE_PTN_S = "[A-Z]{3,8}|911O";
  private static final Pattern OPT_CODE_PTN = Pattern.compile(CODE_PTN_S + "|None|");
  private static final Pattern CODE_ADDR_PTN = Pattern.compile('(' + CODE_PTN_S + ") +(.*)");

  private class MyCodeField extends CodeField {

    public MyCodeField() {
      setPattern(OPT_CODE_PTN, true);
    }

    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      if (field.equals(data.strCode))return;
      data.strCode = append(data.strCode, "/", field);
    }
  }

  private class MyCodeAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_ADDR_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      if (field.equals(data.strCall))return;
      data.strCall = append(data.strCall, "/", field);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - Log - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }
}
