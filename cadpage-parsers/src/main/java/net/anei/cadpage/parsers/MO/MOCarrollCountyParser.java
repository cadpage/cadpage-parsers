package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class MOCarrollCountyParser extends FieldProgramParser {

  public MOCarrollCountyParser() {
    this("CARROLL COUNTY", "MO");
  }

  public MOCarrollCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "Call_Type:CALL! Priority:PRI! Phone:PHONE? Location:ADDRCITYST! Location_Note:INFO/n? Caller__Nickname:NAME Caller__Phone:PHONE Caller__Location:SKIP Call_Notes:INFO/N END");
  }

  @Override
  public String getFilter() {
    return "info@plottlabs.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("Call ([A-Z]{3,5}(?:\\d{4}|-\\d\\d)-\\d{5,9}|CCAD-MIH\\d{4}-null) (?:- (\\S+) )?.*");
  private static final Pattern TIMES_UNIT_PTN = Pattern.compile("\n *\n *\n(\\S+) -   *");
  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strUnit = getOptGroup(match.group(2));

    match = TIMES_UNIT_PTN.matcher(body);
    if (match.find()) {
      data.msgType = MsgType.RUN_REPORT;
      String times = body.substring(match.end()).trim();
      body = body.substring(0, match.start()).trim();
      if (data.strUnit.isEmpty()) data.strUnit = match.group(1);
      data.strSupp = MSPACE_PTN.matcher(times).replaceAll("\n");
    }
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "ID UNIT " + super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("PHONE")) return new MyPhoneField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" - SubType: ", " - ");
      super.parse(field, data);
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int pt = field.indexOf(", Apt:");
      if (pt >= 0) {
        apt = field.substring(pt+6).trim();
        field = field.substring(0, pt).trim();
      }
      field = stripFieldEnd(field, ", United States");
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY ST APT";
    }
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strPhone.isEmpty()) return;
      super.parse(field, data);
    }
  }
}
