package net.anei.cadpage.parsers.MT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTLewisAndClarkCountyCParser extends FieldProgramParser {

  public MTLewisAndClarkCountyCParser() {
    super("LEWIS AND CLARK COUNTY", "MT",
          "ID ( ADDRCITYST/Z INFO_CODE CALL/L CODE/L CALL/L NONE_DATETIME INFO " +
             "| CODE ADDRCITYST PLACE GPS1 GPS2 CODE/L CALL CODE/L CALL/L ( NAME/Z PHONE/Z DATETIME | DATETIME ) INFO EMPTY? " +
             "| CODE_ADDRCITYST CODE CALL/L CALL/L NONE_DATETIME INFO " +
             ") UNIT EMPTY! INFO/N+ END");
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
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("CFS - Incident Code Changed - ") &&
        !body.contains("|")) {
      return codeChangeAlert(body, data);
    }
    body = body.replace('\n', ' ');
    body = body.replace("|INFORMATION PAGE   ", "|INFORMATION PAGE|");
    return parseFields(body.split("\\|",-1), data);
  }

  private static final Pattern CODE_CHANGE_PTN = Pattern.compile("(\\d{6}-\\d{3}) +(\\S+)");

  private boolean codeChangeAlert(String body, Data data) {
    setFieldList("ID CODE CALL INFO");
    int pt = body.indexOf('\n');
    if (pt >= 0) {
      String info = body.substring(pt+1).trim();
      body = body.substring(0,pt).trim();
      data.strSupp = INFO_BRK_PTN.matcher(info).replaceAll("\n").trim();
    }
    Matcher match = CODE_CHANGE_PTN.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strCode = match.group(2);
    data.strCall = "Incident code change";
    return true;
  }

  @Override
  public boolean parseFields(String[] fields, Data data) {
    for (int ndx = 0; ndx < fields.length; ndx++) {
      fields[ndx] = stripFieldEnd(fields[ndx].trim(), "None");
    }
    return super.parseFields(fields, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{6}-\\d{3}");
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO_CODE")) return new CodeField("(?:INFO|ANIMAL)", true);
    if (name.equals("CODE_ADDRCITYST")) return new MyCodeAddressCityStateField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("NONE_DATETIME")) return new DateTimeField("(?:(?:None|\\**INFORMATION PAGE\\**) +)?(\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d)", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final String CODE_PTN_S = "\\d?[A-Z]{3,9}|911[HO]|STRUCTURE FIRE";
  private static final Pattern OPT_CODE_PTN = Pattern.compile(CODE_PTN_S + "|None|");
  private static final Pattern CODE_ADDR_PTN = Pattern.compile('(' + CODE_PTN_S + ") +(.*)");

  private class MyCodeField extends CodeField {

    public MyCodeField() {
      setPattern(OPT_CODE_PTN, true);
    }

    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      if (field.equals("STRUCTURE FIRE")) {
        if (field.equals(data.strCall))return;
        data.strCall = append(data.strCode, "/", field);
      } else {
        if (field.equals(data.strCode))return;
        data.strCode = append(data.strCode, "/", field);
      }
    }
  }

  private static final Pattern ADDR_TRL_PTN = Pattern.compile("(.*?)[, ]+TRL (.*)", Pattern.CASE_INSENSITIVE);

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      Matcher match = ADDR_TRL_PTN.matcher(data.strAddress);
      if (match.matches()) {
        data.strAddress = match.group(1).trim();
        data.strApt = append(match.group(2).trim(), "-", data.strApt);
      }
    }
  }

  private class MyCodeAddressCityStateField extends MyAddressCityStateField {
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
