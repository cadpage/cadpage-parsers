package net.anei.cadpage.parsers.VA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Albemarle County, VA (B)
 */

public class VAAlbemarleCountyBParser extends FieldProgramParser {

  public VAAlbemarleCountyBParser() {
    super("ALBEMARLE COUNTY", "VA",
          "( HTML_ADDRCITY NATURE_UNIT_CROSS! INFO/N+ " +
          "| SRC! INC:ID! TYP:CALL1! UNITS:UNIT! AD:ADDRCITY% APT:APT% CROSS_STREETS:X% LAT:GPS1 LON:GPS2 NAME:NAME% NATURE:CALL/SDS% NARRATIVE:INFO/N+ ESN:BOX% FDSUTalkgroup:CH DT:DATETIME% END )");
    setupProtectedNames("LEWIS AND CLARK");
  }

  @Override
  public String getFilter() {
    return "cad2@acuecc.org,@albemarle.org,powellbr@charlottesville.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    data.strCall = subject.substring(27).trim();
    if (!parseFields(body.split("\n"), data)) return false;

    // Apt is usually duplicated at end of address
    if (data.strApt.length() > 0) {
      data.strAddress = stripFieldEnd(data.strAddress, ' ' + data.strApt);
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("HTML_ADDRCITY")) return new MyHtmlAddressCityField();
    if (name.equals("NATURE_UNIT_CROSS")) return new MyNatureUnitCrossField();
    if (name.equals("SRC")) return new SourceField("[A-Za-z0-9 ]+", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("CALL1")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern HTML_ADDR_CITY_PTN = Pattern.compile("http://maps.google.com/.* AD: *(.*)");
  private class MyHtmlAddressCityField extends MyAddressCityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = HTML_ADDR_CITY_PTN.matcher(field);
      if (!match.matches()) return false;
      super.parse(match.group(1), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CALL " + super.getFieldNames();
    }
  }

  private static final Pattern NATURE_UNIT_CROSS_PTN = Pattern.compile("([^,]*),(.*),([^,]*),");
  private class MyNatureUnitCrossField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = NATURE_UNIT_CROSS_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = append(data.strCall, " - ", match.group(1).trim());
      data.strUnit = match.group(2).trim();
      data.strCross = match.group(3).trim();
    }

    @Override
    public String getFieldNames() {
      return "CALL UNIT X";
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("[", "").replace("]", "").trim();
      if (field.startsWith("Incident not yet created")) return;
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strCall.startsWith(field)) {
        data.strCall = field;
      }
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [ap]m)?)", Pattern.CASE_INSENSITIVE);
  private static DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2).toUpperCase();
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }

  private static final Pattern INFO_HEAD_PTN = Pattern.compile("\\*+\\d\\d?/\\d\\d?/\\d{4}\\*+\\d\\d:\\d\\d:\\d{2,}[A-Z]+- +");
  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +\\d\\d:\\d\\d:\\d{2,}[A-Z]+- +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HEAD_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      for (String part : INFO_BRK_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
  }
}
