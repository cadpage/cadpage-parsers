package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class TXDallasCountyDParser extends HtmlProgramParser {

  public TXDallasCountyDParser() {
    super("DALLAS COUNTY", "TX",
          "CALL! SKIP+ Reporting_Party:NAME_PHONE! Location:ADDRCITYST! Disposition:CALL2! INFO/N+? TIMES+");
  }

  @Override
  public String getFilter() {
    return "notification@arms-mail.com";
  }

  private String times = null;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.equals("ARMS CAD Notification")) return false;
    times = "";
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(times, "\n", data.strSupp);
    }
    times = null;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(?:(.*) - )?([-0-9]+)");

  private class MyNamePhoneField extends NameField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = getOptGroup(match.group(1));
        data.strPhone = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }

  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile(", *[A-Z]{2}(?: +\\d{5})? *,");

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ST_ZIP_PTN.matcher(field);
      if (match.find()) field = field.substring(0, match.end()-1);
      int pt = field.lastIndexOf(" - ");
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+3).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(field, " - ", data.strCall);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("(?:Police Information|Public Information|Arrest|Media Log):.*");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("Rec`d: \\d\\d:\\d\\d +");

  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Recd:")) return false;
      if (INFO_JUNK_PTN.matcher(field).matches()) return true;

      for (String line : field.split("\n")) {
        line = line.trim();
        Matcher match = INFO_HDR_PTN.matcher(line);
        if (match.lookingAt()) line = line.substring(match.end());
      }

      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("([A-Za-z]+): *[:0-9]+");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIMES_PTN.matcher(field);
      if (!match.matches()) return;
      times = append(times, "\n", field);
      if (match.group(1).equals("Clear")) data.msgType = MsgType.RUN_REPORT;
    }
  }
}
