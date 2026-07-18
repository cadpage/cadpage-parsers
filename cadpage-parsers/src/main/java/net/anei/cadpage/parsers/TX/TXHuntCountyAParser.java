package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXHuntCountyAParser extends FieldProgramParser {

  public TXHuntCountyAParser() {
    super("HUNT COUNTY", "TX",
          "PRI! ( INC_#:ID! Run_#:ID/L! Chief_Complaint:CALL! Address:ADDRCITYST! Resources:UNIT! INFO/R! INFO/N+ " +
               "| Chief_Complaint:CALL! Criteria_Code:CODE! Address:ADDRCITYST! Resources:UNIT! INC_#:ID! Run_#:ID/L! " +
               ") Notes:INFO/N! INFO/N+");
  }

  public TXHuntCountyAParser(String defCity, String defState) {
    super(defCity, defState,
          "PRI! ( INC_#:ID! Run_#:ID/L! Chief_Complaint:CALL! Address:ADDRCITYST! Resources:UNIT! INFO/R! INFO/N+ " +
               "| Chief_Complaint:CALL! Criteria_Code:CODE! Address:ADDRCITYST! Resources:UNIT! INC_#:ID! Run_#:ID/L! " +
               ") Notes:INFO/N! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "logissmtp@emsc.net";
  }

  private static final Pattern MISSING_BRK_MARK_PTN = Pattern.compile("[A-Z0-9+]+\\*?(?:Chief Complaint:|INC #:)");
  private static final Pattern MISSING_BRK_PTN = Pattern.compile("(?<!\n)(?=(?:Chief Complaint|Criteria Code|Address|Resources|INC #|Run #|Notes|Created|Dispatched|Enroute|At Scene|Available):)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    boolean cancel = subject.startsWith("Incident Cancelled - ");

    if (MISSING_BRK_MARK_PTN.matcher(body).lookingAt()) {
      body = body.replace("RUN #:", "Run #:");
      body = MISSING_BRK_PTN.matcher(body).replaceAll("\n");
    }

    if (!parseFields(body.split("\n"), data)) return false;
    if (cancel) data.strCall = append("Cancel", " - ", data.strCall);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private static final Pattern ADDR_ST_PTN = Pattern.compile(", *TX\\b");

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (field.toUpperCase().endsWith("RES")) {
        field = field.substring(0, field.length()-3).trim();
      }
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt >= 0) {
          String temp = field.substring(pt+1, field.length()-1).trim();
          if (ADDR_ST_PTN.matcher(temp).find()) {
            String place = field.substring(0, pt).trim();
            if (!place.equals(temp)) data.strPlace = place;
            field = temp;
          }
        }
      }
      String apt = "";
      if (field.endsWith(")")) {
        int pt = field.lastIndexOf('(');
        if (pt >= 0) {
          String temp = field.substring(pt+1, field.length()-1).trim();
          field = field.substring(0, pt).trim();
          if (temp.startsWith("Apt. # ")) {
            apt = temp.substring(7).trim();
          }
        }
      }
      int pt = field.indexOf(" Apt. #");
      if (pt >= 0) {
        apt = field.substring(pt+7).trim();
        field = field.substring(0, pt+1).trim();
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }

  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = PRIVATE_PTN.matcher(addr).replaceAll("PVT");
    addr = PRNNN_PTN.matcher(addr).replaceAll("PVT ROAD $1");
    return addr;
  }
  private static final Pattern PRIVATE_PTN = Pattern.compile("\\bPRIVATE\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern PRNNN_PTN = Pattern.compile("\\bPR *(\\d+)\\b", Pattern.CASE_INSENSITIVE);
}
