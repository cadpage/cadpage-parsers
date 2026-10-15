package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchC15Parser extends HtmlProgramParser {

  public DispatchC15Parser(String defCity, String defState) {
    super(defCity, defState,
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
    if (name.equals("NAME_PHONE")) return new BaseNamePhoneField();
    if (name.equals("ADDRCITYST")) return new BaseAddressCityStateField();
    if (name.equals("CALL2")) return new BaseCall2Field();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("TIMES")) return new BaseTimesField();
    return super.getField(name);
  }

  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(?:(.*) - )?([-0-9]+)");

  private class BaseNamePhoneField extends NameField {

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

  private class BaseAddressCityStateField extends AddressCityStateField {
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

  private class BaseCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(field, " - ", data.strCall);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("(?:Police Information|Public Information|Arrest|Media Log):.*");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("Rec`d: \\d\\d:\\d\\d +");

  private class BaseInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Recd:")) return false;
      if (INFO_JUNK_PTN.matcher(field).matches()) return true;

      if (field.startsWith("Incoming ANI/ALI Record:")) {
        int pt = field.indexOf("QUERY CALLER FOR PHONE # ");
        if (pt >= 0) {
          setGPSLoc(field.substring(pt+25), data);
          return true;
        }
      }

      if (field.endsWith(": ASSIGNED CALL")) {
        data.strUnit = field.substring(0, field.length()-15).trim();
        return true;
      }

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

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS UNIT";
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("([A-Za-z]+): *[:0-9]+");
  private class BaseTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIMES_PTN.matcher(field);
      if (!match.matches()) return;
      times = append(times, "\n", field);
      if (match.group(1).equals("Clear")) data.msgType = MsgType.RUN_REPORT;
    }
  }
}
