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
          "Response:CALL! Chief_Complaint:CALL/S Criteria_Code:CODE! Address:ADDRCITYST! Resources:UNIT! INC_#:ID? Run_#:ID/L! " +
              "Notes:INFO! INFO/N+ Tags:EMPTY? Status_Times:TIMES");
  }

  public String getAliasCode() {
    return "AZYavapaiCountyE";
  }

  @Override
  public String getFilter() {
    return "logissmtp@emsc.net";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(New Incident|Update to Incident|Incident Completed|Incident Cancelled) - (\\d+)");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=(?:Chief Complaint|Criteria Code|Address|Resources|Run #|Notes):)");

  private String times;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    String type = null;
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) type = match.group(1);
    times = "";
    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    if (!super.parseMsg(body, data)) return false;
    if (type != null) {
      if (type.equals("Incident Completed")) {
        data.msgType = MsgType.RUN_REPORT;
        data.strSupp = append(times, "\n", data.strSupp);
      } else if (type.equals("Incident Cancelled")) {
        data.strCall = append("Cancelled", " - ", data.strCall);
      }
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
      for (String line : field.split("\n")) {
        line = line.trim();
        if (line.equals("End")) return;
        if (line.endsWith(":")) return;
        if (line.startsWith("Finished:")) data.msgType = MsgType.RUN_REPORT;
        times = append(times, "\n", line);
      }
    }
  }
}
