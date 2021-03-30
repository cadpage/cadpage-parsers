package net.anei.cadpage.parsers.MD;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDCharlesCountyBParser extends FieldProgramParser {

  public MDCharlesCountyBParser() {
    super("CHARLES COUNTY", "MD",
           "CALL ADDR URL! ID UNIT DATETIME INFO+");
  }

  @Override
  public String getFilter() {
    return "@sms.mdfiretech.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    return parseFields(body.split("\n"), 3, data);
  }

  @Override
  public String adjustMapAddress(String address) {
    int pt = address.indexOf('(');
    if (pt >= 0) address = address.substring(0,pt).trim();
    address = PR_PTN.matcher(address).replaceAll("PRINCE");
    return address;
  }
  private static final Pattern PR_PTN = Pattern.compile("\\bPR\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("URL")) return new MyInfoUrlField();
    if (name.equals("ID")) return new IdField("\\d+", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM) *(.*)|\\d+[A-Z]?|[A-Z]");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      String extra = null;
      while (true) {
        int pt = field.indexOf(',');
        if (pt <= 0) break;
        extra = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (!field.equals("PG COUNTY")) break;
        data.strCity = "PRINCE GEORGES COUNTY";
        field = extra;
        extra = null;
      }
      super.parse(field, data);

      if (extra == null) return;

      Matcher match = APT_PTN.matcher(extra);
      if (match.matches()) {
        data.strApt = match.group(1);
        if (data.strApt == null) data.strApt = extra;
        return;
      }

      extra = extra.replace('@', '/');
      if (extra.contains("/")) {
        data.strCross = extra;
        return;
      }

      data.strPlace = extra;
    }

    @Override
    public String getFieldNames() {
      return "CITY " + super.getFieldNames() + " APT X PLACE";
    }
  }

  private class MyInfoUrlField extends InfoUrlField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("mdfire.com/?") || field.startsWith("mdft.us/?")) {
        field = "http://" + field;
      } else if (!field.startsWith("http://") && !field.startsWith("https://")) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4}) +(\\d{1,2}:\\d{1,2}:\\d{1,2} [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return;
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
}
