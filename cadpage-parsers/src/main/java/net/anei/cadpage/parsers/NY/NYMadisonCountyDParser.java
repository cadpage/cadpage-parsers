package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NYMadisonCountyDParser extends FieldProgramParser {

  public NYMadisonCountyDParser() {
    super(NYMadisonCountyParser.CITY_LIST, "MADISON COUNTY", "NY",
          "DATETIME! EMPTY! ID! INCIDENT_LOCATION:ADDRCITY! CROSS_STREETS:X! ZONE:MAP! NATURE:CODE_CALL! PRIORITY:PRI! EMD_DETERMINANT:CODE! CALL_COMMENTS:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "e911@madisoncounty.ny.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident Notification")) return false;
    return parseFields(body.split("\n"), data);
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("TIME - ([A-Z][a-z][a-z] [A-Z][a-z][a-z] \\d\\d \\d\\d:\\d\\d:\\d\\d \\d{4})", DATE_TIME_FMT, true);
    if (name.equals("ID")) return new IdField("INCIDENT # +(.*)", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, field, data);
      field  = getStart();
      int pt = field.indexOf(';');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      parseAddress(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d\\d)-(.*)");
  private class MyCodeCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2).trim();
      }
      super.parse(field, data);
    }
  }

  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      super.parse(field,  data);
      String call = CALL_CODES.getCodeDescription(field);
      if (call != null) data.strCall = call;
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d [A-Za-z ]+:");
  private static final Pattern INFO_PHONE_PTN = Pattern.compile("WPH\\d data\\. Phone: (\\S*?), UNC: \\d, Lat: (\\S*?), Long: (\\S*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      Matcher match = INFO_PHONE_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone =  match.group(1);
        setGPSLoc(match.group(2)+','+match.group(3), data);
      }
      else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "PHONE GPS " + super.getFieldNames();
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("TRNPK", "TURNPIKE");
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable();
}
