package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHButlerCountyCParser extends FieldProgramParser {

  public OHButlerCountyCParser() {
    super("BUTLER COUNTY", "OH",
          "CALL ( ID1! Address:ADDRCITY! Cross_Streets:X! DATETIME! Area:MAP! Plan:MAP/L! Caller:NAME_PHONE1! Cautions:ALERT! Units:UNIT! INFO! INFO+ " +
               "| ADDRCITY X UNIT! INFO! DATETIME! Caller:NAME_PHONE2! CAD#:ID! Call_Taker:SKIP! GPS! EMPTY! " +
               ") END");
  }

  @Override
  public String getFilter() {
    return "noreply@butlersheriff.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile(" *\\*{2} *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("BCSO Dispatch")) return false;
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID1")) return new IdField("Incident # +(.*)", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("NAME_PHONE1")) return new MyNamePhone1Field();
    if (name.equals("NAME_PHONE2")) return new MyNamePhone2Field();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[, ]*\\[\\d\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "[end]");
      field = stripFieldEnd(field, ",");
      field = stripFieldEnd(field, "[shared]");
      for (String line : INFO_BRK_PTN.split(field)) {
        line = stripFieldStart(line, "[Query]");
        data.strSupp = append(data.strSupp, "\n", line);
      }

    }
  }

  private static final Pattern NAME_PHONE1_PTN = Pattern.compile("(.*) (\\(\\d{3}\\) ?\\d{3}-\\d{4})\\b.*");
  private class MyNamePhone1Field extends NameField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE1_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPhone = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }

  }

  private class MyNamePhone2Field extends NameField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        data.strPhone = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("X=(\\d{2})(\\d{6}) , Y=(\\d{2})(\\d{6})");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("X=0 , Y=0")) return;
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      setGPSLoc(match.group(1)+'.'+match.group(2)+','+match.group(3)+'.'+match.group(4), data);
    }
  }
}
