package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

public class NCWakeCountyDParser extends FieldProgramParser {

  public NCWakeCountyDParser() {
    super("WAKE COUNTY", "NC",
          "Call:CALL! Place:PLACE! Addr:ADDR! City:CITY! ID:ID! Pri:PRI! Date:DATE! Time:TIME! Map:MAP! Unit:UNIT! Info:INFO! TAC:CH");
  }

  @Override
  public String getFilter() {
    return "ECCDISPATCH@rwecc.net";
  }

  private static final Pattern DELIM = Pattern.compile(" *(?=(?:Place|Addr|City|ID|Pri|Date|Time|Map|Unit|Info|TAC):)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body),  data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final CodeTable CODE_TABLE = new StandardCodeTable();

  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*?) (\\d\\d?[A-Z]\\d\\d?[A-Z]?)");

  private class MyCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(2);
        data.strCall = CODE_TABLE.getCodeDescription(data.strCode);
        if (data.strCall == null) data.strCall = match.group(1).trim();
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[, ]*\\[\\d{1,2}\\] +");
  private static final Pattern INFO_SUBBRK_PTN = Pattern.compile(" +(?=\\d{1,2}\\. +)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : INFO_BRK_PTN.split(field)) {
        if (part.length() == 0) continue;
        if (part.startsWith("Automatic Case Number(s)")) continue;
        if (part.startsWith("Class of Service:")) continue;
        if (part.startsWith("LAT:")) {
          setGPSLoc(part, data);
          continue;
        }
        part = INFO_SUBBRK_PTN.matcher(part).replaceAll("\n");
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }

    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }
}
