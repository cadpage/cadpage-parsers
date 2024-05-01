package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * This parser handles a subclass of HTML parsers with some common attributes
 *
 *  1) They all include a subject of Automatic R&R Notification: xxxx
 *  2) One of two different specialized INFO_BLK blocks where
 *     dispatcher
 *     dash
 *     information
 *  are either combined in one field or split into 3 different fields
 *
 *  3) A common TIMES format reporting unit dispatch times
 */

public class DispatchH05Parser extends HtmlProgramParser {

  private boolean accumulateUnits = false;

  public DispatchH05Parser(String defCity, String defState, String program) {
    this((String[])null, defCity, defState, program, null);
  }

  public DispatchH05Parser(String[] cityList, String defCity, String defState, String program) {
    this(cityList, defCity, defState, program, null);
  }

  public DispatchH05Parser(Properties cityCodes, String defCity, String defState, String program) {
    this(cityCodes, defCity, defState, program, null);
  }

  public DispatchH05Parser(String defCity, String defState, String program, String userTags) {
    this((String[])null, defCity, defState, program, userTags);
  }

  public DispatchH05Parser(String[] cityList, String defCity, String defState, String program, String userTags) {
    super(cityList, defCity, defState, program, userTags);
  }

  public DispatchH05Parser(Properties cityCodes, String defCity, String defState, String program, String userTags) {
    super(cityCodes, defCity, defState, program, userTags);
  }

  public void setAccumulateUnits(boolean accumulateUnits) {
    this.accumulateUnits = accumulateUnits;
  }

  private String times;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification")) return false;
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strCall.length() == 0) {
      data.strCall = stripFieldStart(subject.substring(26).trim(), ":");;
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "CALL? " + super.getProgram();
  }

  @Override
  protected boolean parseFields(String[] flds, Data data) {
    infoFirst = true;
    times = null;
    if (!super.parseFields(flds, data)) return false;
    if (data.msgType == MsgType.RUN_REPORT && times != null) {
      data.strSupp = append(times, "\n\n", data.strSupp);
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYAPT")) return new BaseAddressCityAptField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("ID")) return new BaseIdField();
    if (name.equals("INFO_BLK")) return new BaseInfoBlockField();
    if (name.equals("TIMES")) return new BaseTimesField();
    return super.getField(name);
  }

  private static final Pattern CITY_APT_PTN = Pattern.compile("(?:([A-Z ]+?) +)??(?:(?:APT|#) *(.*)|([A-Z]?\\d+[A-Z]?|[A-Z]|))", Pattern.CASE_INSENSITIVE);

  private class BaseAddressCityAptField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher match = CITY_APT_PTN.matcher(city);
      String apt = "";
      if (match.matches()) {
        city = match.group(1);
        apt = match.group(2);
        if (apt == null) apt = match.group(3);
        if (city == null) city = p.getLastOptional(',');
      }
      data.strCity = city;

      field = p.get().replace('@', '&');
      parseAddress(StartType.START_ADDR, FLAG_ALLOW_DUAL_DIRECTIONS | FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      if (apt.contains(data.strApt)) {
        data.strApt = apt;
      } else if (!data.strApt.contains(apt)) {
        data.strApt = append(data.strApt, "-", apt);
      }
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private class BaseIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(",")) {
        part = part.trim();
        if (part.startsWith("[Incident not yet created")) continue;
        data.strCallId = append(data.strCallId, ", ", part);
      }
    }
  }

  private boolean infoFirst = true;
  private boolean infoEnabled = false;

  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("\\*+\\d\\d?/\\d\\d?/\\d{4}\\*+|\\d\\d?:\\d\\d:\\d\\d");
  private static final Pattern INFO_TIMES_MARK_PTN = Pattern.compile("[A-Z0-9]+: .*");
  protected class BaseInfoBlockField extends InfoField {

    private boolean optInfo = false;

    @Override
    public void setQual(String qual) {
      super.setQual(qual);
      optInfo = qual != null && qual.contains("I");
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (INFO_TIMES_MARK_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {

      if (field.equals(";")) return;

      if (infoFirst) {
        infoEnabled = optInfo;
        infoFirst = false;
      }

      if (INFO_DATE_TIME_PTN.matcher(field).matches()) {
        infoEnabled = false;
        return;
      }

      if (field.equals("-")) {
        infoEnabled = true;
        return;
      }
      if (!infoEnabled) {
        int pt = field.indexOf(" - ");
        if (pt >= 0) {
          field = field.substring(pt+3).trim();
          infoEnabled = true;
        }
      }

      if (infoEnabled) {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }
  }

  private class BaseTimesField extends InfoField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (INFO_DATE_TIME_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {

      if (accumulateUnits && field.startsWith("Unit:")) {
        data.strUnit = append(data.strUnit, ",", field.substring(5).trim());
      }

      if (field.startsWith("Cleared:") || field.startsWith("Cleared at:")) data.msgType = MsgType.RUN_REPORT;
      if (times == null) {
        times = field;
      } else {
        times = append(times, "\n", field);
      }
    }

    @Override
    public String getFieldNames() {
      String result = super.getFieldNames();
      if (accumulateUnits) result = "UNIT " + result;
      return result;
    }
  }
}
