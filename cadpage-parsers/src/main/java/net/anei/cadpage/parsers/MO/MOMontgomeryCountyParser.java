package net.anei.cadpage.parsers.MO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MOMontgomeryCountyParser extends FieldProgramParser {

  public MOMontgomeryCountyParser() {
    super("MONTGOMERY COUNTY", "MO",
          "HDR SKIP ID ADDRCITYST! X! Agency_Name:SRC! Category:CALL! TIMES+? NAME_PHONE! PLACE! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  private String times;

  @Override
  protected boolean parseMsg(String body, Data data) {
    times = "";
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT)data.strSupp = append(times, "\n", data.strSupp);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("HDR")) return new SkipField("Montgomery County Communication Center", true);
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{5}", true);
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_CITY_PTN = Pattern.compile("(\\d+[A-Z]?) +(.*)");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      Matcher match = APT_CITY_PTN.matcher(data.strCity);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        data.strCity = match.group(2);
      }
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("(Opened Date / Time|[A-Za-z]+): *(.*)");
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
  private class MyTimesField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = TIMES_PTN.matcher(field);
      if (!match.matches()) return false;
      times = append(times, "\n", field);
      String key = match.group(1);
      String val = match.group(2);
      if (!val.isEmpty()) {
        if (key.equals("DispatchDateTime")) {
          setDateTime(DATE_TIME_FMT, val, data);
        } else if (key.equals("DepartureDateTime")) {
          data.msgType = MsgType.RUN_REPORT;
        }
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO?";
    }
  }

  private static final Pattern NAME_PHONE_PTN = Pattern.compile("([A-Z, ]*?) *(\\(\\d{3}\\) \\d{3}-\\d{4})");
  private class MyNamePhoneField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPhone = match.group(2);
      }
      data.strName = field;
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Call Received on ") || field.startsWith("Location:")) return;
      super.parse(field, data);
    }
  }
}
