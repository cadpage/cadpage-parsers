package net.anei.cadpage.parsers.AZ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class AZYavapaiCountyEParser extends FieldProgramParser {

  public AZYavapaiCountyEParser() {
    this("YAVAPAI COUNTY", "AZ");
  }

  public AZYavapaiCountyEParser(String defCity, String defState) {
    super(defCity, defState,
          "Response:CALL! Chief_Complaint:CALL/S Criteria_Code:CODE! Address:ADDRCITYST! Resources:UNIT! INC_#:ID! Run_#:ID/L! " +
              "Notes:INFO! INFO/N+ Tags:EMPTY! Status_Times:EMPTY! TIMES+");
  }

  public String getAliasCode() {
    return "AZYavapaiCountyE";
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

  private static final Pattern AZ_PTN = Pattern.compile(", *AZ\\b");

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) {
        String newAddr = stripFieldEnd(field.substring(pt+1), ")");
        if (AZ_PTN.matcher(newAddr).find()) {
          String place = field.substring(0,pt).trim();
          if (!place.equals(newAddr)) data.strPlace = place;
          field = newAddr;
        }
      }
      Parser p = new Parser(field);
      String apt = p.getLastOptional(" Apt. #");
      p.getLastOptional('(');
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
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
