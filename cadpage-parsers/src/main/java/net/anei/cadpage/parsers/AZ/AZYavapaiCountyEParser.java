package net.anei.cadpage.parsers.AZ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class AZYavapaiCountyEParser extends FieldProgramParser {

  public AZYavapaiCountyEParser() {
    super("YAVAPAI COUNTY", "AZ",
          "Response:CALL! Chief_Complaint:CALL/S Criteria_Code:CODE! Address:ADDRCITYST! Resources:UNIT! INC_#:ID! Run_#:ID/L! " +
              "Notes:INFO! INFO/N+ Tags:EMPTY! Status_Times:EMPTY! TIMES+");
  }

  @Override
  public String getFilter() {
    return "logissmtp@emsc.net";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(New Incident|Update to Incident|Incident Completed|Incident Cancelled) - (\\d+)");

  private String times;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    String type = match.group(1);
    times = "";
    if (!parseFields(body.split("\n"), data)) return false;
    if (type.equals("Incident Completed")) {
      data.msgType = MsgType.RUN_REPORT;
      data.strSupp = append(times, "\n", data.strSupp);
    } else if (type.equals("Incident Cancelled")) {
      data.strCall = append("Cancelled", " - ", data.strCall);
    }

    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String apt = p.getLastOptional(" Apt. #");
      p.getLastOptional('(');
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("End")) return;
      if (field.endsWith(":")) return;
      times = append(times, "\n", field);
    }
  }
}
