package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

public class NCWakeCountyEParser extends FieldProgramParser {

  public NCWakeCountyEParser() {
    super("WAKE COUNTY", "NC",
          "Incident:CALL! Alarm_Level:PRI! Place:PLACE! Map:MAP! Addr:ADDR! Apt:APT! City:CITY! ID:ID! Pri:PRI/L! Date:DATE! Time:TIME! Unit:UNIT! Info:INFO! TAC:CH! Lati:GPS1/d Long:GPS2/d");
  }

  @Override
  public String getFilter() {
    return "ECCDISPATCH@rwecc.net";
  }

  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=(?:Alarm Level|Place|Map|Addr|City|ID|Pri|Time|Unit|Info|TAC|Lati|Long):)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("\n ", "");
    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.+) (\\d{1,2}[A-Z]\\d{1,2}[A-Z]?)");
  private CodeTable CALL_CODES = new StandardCodeTable();

  private class MyCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCode = match.group(2);
        String call = CALL_CODES.getCodeDescription(data.strCode);
        if (call != null) field = call;
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

  private static final Pattern INFO_DELIM = Pattern.compile("[, ]*\\[\\d{1,2}\\] *");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("Class of Service:.*|Automatic Case Number.*");
  private static final Pattern INFO_DELIM2 = Pattern.compile(" +(?=\\d{1,2}\\. +)");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      StringBuilder sb = new StringBuilder();
      for (String line : INFO_DELIM.split(field)) {
        if (INFO_JUNK_PTN.matcher(line).matches()) continue;
        if (line.startsWith("LAT:")) {
          setGPSLoc(line, data);
          continue;
        }
        for (String part : INFO_DELIM2.split(line)) {
          if (sb.length() > 0) sb.append('\n');
          part = stripFieldEnd(part, ",");
          sb.append(part);
        }
      }
      data.strSupp = sb.toString();
    }

    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }
}
