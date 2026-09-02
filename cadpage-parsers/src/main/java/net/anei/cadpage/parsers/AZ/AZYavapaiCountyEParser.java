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
  private static final Pattern ZIP_CITY_PTN = Pattern.compile("[-0-9]+ +(.*)");

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

    match = ZIP_CITY_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private static final Pattern PLACE_APDX_PTN = Pattern.compile("[^,()]*\\)[^,()]*\\(");
  private static final Pattern AZ_PTN = Pattern.compile(", *AZ\\b");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("([A-Z].*? \\d+) ([A-Z]?\\d+)");
  private static final Pattern HWY_NN_PTN = Pattern.compile("(?:[NSEW] +)(?:HWY|HIGHWAY|US|AZ) +\\d+", Pattern.CASE_INSENSITIVE);
  private static final Pattern BACKWARD_ADDR_PTN = Pattern.compile("([A-Z].*?) +(\\d+)");

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) {
        String newAddr = stripFieldEnd(field.substring(pt+1), ")");
        Matcher match = PLACE_APDX_PTN.matcher(newAddr);
        if (match.lookingAt()) {
          pt += match.end();
          newAddr = field.substring(pt+1).trim();
        }
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

      Matcher match = ADDR_APT_PTN.matcher(data.strAddress);
      if (match.matches()) {
        String addr = match.group(1);
        if (!HWY_NN_PTN.matcher(addr).matches()) {
          data.strAddress = addr;
          data.strApt = match.group(2);
        }
      }

      match = BACKWARD_ADDR_PTN.matcher(data.strAddress);
      if (match.matches()){
        data.strAddress = match.group(2) + ' ' + match.group(1);
      }

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
