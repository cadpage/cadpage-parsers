package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class TXHuntCountyAParser extends FieldProgramParser {

  public TXHuntCountyAParser() {
    this("HUNT COUNTY", "TX");
  }

  public TXHuntCountyAParser(String defCity, String defState) {
    super(defCity, defState,
          "( Call:CODE_CALL Incident:ID! Address:ADDRCITYST Coordinates:GPS! Address_Comment:SKIP! Resource:UNIT! Response:PRI! " +
          "| ID_PRI! Chief_Complaint:CODE_CALL! Criteria_Code:CODE! Address:ADDRCITYST! Resources:UNIT! INC_#:ID! Run_#:ID/L! " +
          "| PRI! ( Chief_Complaint:CODE_CALL! Address:ADDRCITYST! Resources:UNIT! INC_#:ID! Run_#:ID/L! " +
                 "| INC_#:ID! Run_#:ID/L! Chief_Complaint:CODE_CALL! Address:ADDRCITYST! Resources:UNIT! " +
                 ") INFO/N+ " +
          ") Notes:INFO/N! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "logissmtp@emsc.net,logisids@vlitech.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:(?:New|Update(?: to)?) Incident|Incident (?:Cancelled|Completed)) - (\\d+)");
  private static final Pattern MISSING_BRK_MARK_PTN = Pattern.compile("[-A-Z0-9a-z+ ]+\\*?(?:Chief Complaint:|INC #:)");
  private static final Pattern MISSING_BRK_PTN = Pattern.compile("(?<!\n)(?=(?:Chief Complaint|Criteria Code|Address|Resources|INC #|Run #|Notes|Created|Dispatched|Enroute|At Scene|Available):)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    boolean cancel = subject.startsWith("Incident Cancelled - ");
    String callId = null;
    if (!subject.isEmpty()) {
      Matcher match = SUBJECT_PTN.matcher(subject);
      if (!match.matches()) return false;
      callId = match.group(1);
    }

    if (MISSING_BRK_MARK_PTN.matcher(body).lookingAt()) {
      body = body.replace("RUN #:", "Run #:");
      body = MISSING_BRK_PTN.matcher(body).replaceAll("\n");
      data.msgType = MsgType.RUN_REPORT;
    }

    body = body.replace(" Notes:", "\nNotes:");
    if (!parseFields(body.split("\n"), data)) return false;
    if (cancel) data.strCall = append("Cancel", " - ", data.strCall);
    if (callId != null && data.strCallId.isEmpty()) data.strCallId = callId;
    return true;
  }

  @Override
  public String getProgram() {
    return "ID? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ID_PRI")) return new MyIdPriorityField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+) *- *(.+)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ";");
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern ID_PRI_PTN = Pattern.compile("(?:New|Update) Incident - (\\d+)(\\S+)");

  private class MyIdPriorityField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ID_PRI_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCallId = match.group(1);
      data.strPriority = match.group(2);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ID PRI";
    }
  }

  private static final Pattern ADDR_ST_PTN = Pattern.compile(", *TX\\b");
  private static final Pattern ZIP_CITY_PTN = Pattern.compile("[-0-9]+ +(.+)");

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int pt = field.lastIndexOf(')');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt+1);
      }

      if (field.toUpperCase().endsWith("RES")) {
        field = field.substring(0, field.length()-3).trim();
      }
      if (field.endsWith(")")) {
        pt = field.indexOf('(');
        if (pt >= 0) {
          String temp = field.substring(pt+1, field.length()-1).trim();
          if (ADDR_ST_PTN.matcher(temp).find()) {
            String place = field.substring(0, pt).trim();
            if (!place.equals(temp)) data.strPlace = place;
            field = temp;
          }
        }
      }
      if (field.endsWith(")")) {
        pt = field.lastIndexOf('(');
        if (pt >= 0) {
          String temp = field.substring(pt+1, field.length()-1).trim();
          field = field.substring(0, pt).trim();
          if (temp.startsWith("Apt. # ")) {
            apt = append(temp.substring(7).trim(), "-", apt);
          }
        }
      }
      pt = field.indexOf(" Apt. #");
      if (pt >= 0) {
        apt = field.substring(pt+7).trim();
        field = field.substring(0, pt+1).trim();
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);

      Matcher match = ZIP_CITY_PTN.matcher(data.strCity);
      if (match.matches()) data.strCity = match.group(1);
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
