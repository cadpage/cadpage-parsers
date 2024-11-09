package net.anei.cadpage.parsers.TX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXLubbockCountyBParser extends FieldProgramParser {

  public TXLubbockCountyBParser() {
    super("LUBBOCK COUNTY", "TX",
          "ADDR CITY PRI CODE_CALL! CALL+? INFO+? ID1 TIME ID/L Alert:ALERT END");
  }

  @Override
  public String getFilter() {
    return "Dispatch@umchealthsystem.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("RC:")) return false;
    body = body.substring(3).trim();
    int pt = body.indexOf("\nCONFIDENTIALITY NOTICE");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return parseFields(body.split("/"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("Priority (\\d)|(?:AWAITING DETERMINANT|STANDBACK)\\b.*()", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID1")) return new IdField("Run# *(\\d+)", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("(?:P-(\\d) - )?(\\d{1,2}-[A-Z]-\\d{1,2}[A-Z]?) +(.*)");
  private class MyCodeCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        String pri = match.group(1);
        if (pri != null) data.strPriority = pri;
        data.strCode = match.group(2);
        field = match.group(3).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PRI CODE CALL";
    }
  }

  private static final Pattern TRAIL_Y_PTN = Pattern.compile("\\bY$", Pattern.CASE_INSENSITIVE);
  private static final Pattern LEAD_O_PTN = Pattern.compile("^O\\b", Pattern.CASE_INSENSITIVE);
  private class MyCallField extends CallField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!data.strCall.endsWith(" w") && !data.strCall.endsWith(" W") &&
          !(TRAIL_Y_PTN.matcher(data.strCall).find() && LEAD_O_PTN.matcher(field).find()) &&
          !(data.strCall.endsWith(" y") && field.startsWith("o ")) &&
          !CALL_CONT_SET.contains(field)) return false;
      if (data.strCall.contains(field)) return true;
      data.strCall = append(data.strCall, "/", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern PHONE_PTN = Pattern.compile("\\d{10}|\\d{3}[- ]\\d{3}[- ]\\d{4}");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      parseInfo(p.get("ProQA comments:"), data);
      parseInfo(p.get(), data);
    }

    private void parseInfo(String field, Data data) {
      field = stripFieldEnd(field, "-");
      if (field.isEmpty()) return;
      if (data.strPhone.isEmpty() && PHONE_PTN.matcher(field).matches()) {
        data.strPhone = field;
      } else {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "INFO PHONE";
    }
  }

  private static final Set<String> CALL_CONT_SET = new HashSet<String>(Arrays.asList(
      "Childbirth",
      "Chills",
      "Envenomations",
      "Fainting (Near)",
      "ill",
      "Miscarriage",
      "near fainting",
      "Seizures",
      "Stings",
      "Transportation Incidents",
      "Vertigo"
  ));
}
