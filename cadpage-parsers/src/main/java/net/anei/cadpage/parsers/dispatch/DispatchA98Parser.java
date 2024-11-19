package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA98Parser extends FieldProgramParser {

  public DispatchA98Parser(String defCity, String defState) {
    super(defCity, defState,
          "Incident:ID! Station:UNIT? Complaint:CALL! Address_Street:ADDR/S6! Cross_Street:X! Place:PLACE! Latitude:GPS1! Longitude:GPS2 Map:SKIP! Reporting_Unit:SKIP! Units:SKIP! Notes:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "support@smartcop.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.endsWith(" Closed")) {
      data.msgType = MsgType.RUN_REPORT;
    } else if (!subject.endsWith(" Creation")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
