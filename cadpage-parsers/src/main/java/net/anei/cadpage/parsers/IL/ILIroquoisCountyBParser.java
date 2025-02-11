package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class ILIroquoisCountyBParser extends FieldProgramParser {

  public ILIroquoisCountyBParser() {
    super(ILIroquoisCountyParser.CITY_LIST, "IROQUOIS COUNTY", "IL",
          "( SELECT/RR CAD_CFS:ID! Department_CFS:SKIP! Nature:CODE_CALL! Start_Date/Time:SKIP! Units:UNIT! UNIT/C+ Unit_response_times:INFO! INFO/N+ " +
          "| CALL ADDRCITYST/Z INFO! Common_name:PLACE! CAD_Notes:INFO/N! New_Message:INFO! INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "co.iroquois.il.us";
  }

  private static final Pattern RR_DELIM = Pattern.compile(";|, *(?=Start Date/Time:|Units:|Unit response times:)");
  @Override
  protected boolean parseMsg(String body, Data data) {
    setSelectValue("");
    int pt = body.indexOf(" - CAD CFS:");
    if (pt >= 0) {
      data.strSource = body.substring(0,pt).trim();
      body = body.substring(pt+3);
      setSelectValue("RR");
      data.msgType = MsgType.RUN_REPORT;
      return parseFields(RR_DELIM.split(body), data);
    } else {
      return parseFields(body.split(";"), data);
    }
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{4}|[A-Z]{5}) +(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("\\d{4}");

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (!UNIT_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
