package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class NYAlbanyCountyEParser extends FieldProgramParser {

  public NYAlbanyCountyEParser() {
    super("ALBANY COUNTY", "NY",
          "( INCIDENT:ID! BATTALION:SRC! VEHICLE:UNIT! CALL_TYPE:CALL! ADDRESS:ADDR! APPARTMENT:APT! " +
              "LOCATION:PLACE! CROSS_STREETS:X! LATITUDE:GPS1! LONGITUDE:GPS2! TIME_ASSIGNED:DATETIME! COMMENTS:INFO! " +
          "| Incident:ID! Battlion:SRC! Vehicle:UNIT! Call_Type:CALL! Time_Assigned:TIMES! " +
          ") END");
    setBreakChar('|');
  }

  @Override
  public String getFilter() {
    return "paging@cdps911.com,paging@albanysheriffny.gov>";
  }

  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=BATTA?LION|VEHICLE|CALL TYPE|ADDRESS|APPARTMENT|LOCATION|CROSS STREETS|LATITUDE|LONGITUDE|TIME ASSIGNED|COMMENTS)", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("cadpaging")) return false;
    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("\\[\\d{1,2}\\]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String part : INFO_BRK_PTN.split(field)) {
        part = part.trim();
        part = stripFieldEnd(part, ",");
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)");
  private static final Pattern TIMES_SEP_PTN = Pattern.compile(" *\\| *");
  private static final Pattern TIMES_BRK_PTN = Pattern.compile("( *)(?=Enroute|Time Arrived|Call Clear|Time at Hospital)");

  private class MyTimesField extends Field {
    @Override
    public void parse(String field, Data data) {

      data.msgType = MsgType.RUN_REPORT;

      Matcher match = DATE_TIME_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strDate = match.group(1);
        data.strTime = match.group(2);
      }
      field = TIMES_SEP_PTN.matcher(field).replaceAll(": ");
      field = TIMES_BRK_PTN.matcher(field).replaceAll("\n");
      field = "Dispatched: " + field.trim();

      data.strSupp = field;
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }

}
