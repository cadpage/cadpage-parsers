package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchC04Parser extends FieldProgramParser {

  public DispatchC04Parser(String defCity, String defState) {
    super(defCity, defState,
          "CFS:ID! CALL! ADDRCITYST! ADDR2? ( GPS! | X_PLACE GPS! | ) " +
              "( X_PLACE UNIT_TIMES! | UNIT_TIMES! " +
              "| Received:TIMES! " +
              "| X_PLACE PHONE! NAME FAIL Received:TIMES! " +
              "| X_PLACE X_PLACE PHONE! NAME FAIL Received:TIMES! " +
              "| PHONE! NAME FAIL Received:TIMES! " +
              "| NAME! FAIL Received:TIMES! " +
              "| Received:TIMES! " +
              "| X_PLACE Received:TIMES! " +
              "| X PLACE X_PLACE Received:TIMES! " +
              "| X_PLACE NAME! FAIL Received:TIMES! " +
              "| X_PLACE X_PLACE NAME! FAIL Received:TIMES! " +
              "| X_PLACE+ Received:TIMES! " +
              ") TIMES/N+");

  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("iSOMS - ")) return false;
    subject = subject.substring(8).trim();
    if (subject.startsWith("CAD UNIT ")) {
      subject = subject.substring(9).trim();
      int pt  = subject.indexOf(' ');
      if (pt < 0) pt = subject.length();
      data.strSource = subject.substring(0,pt);
      subject = subject.substring(pt).trim();
      if (subject.equals("COMPLETED-CALL")) data.msgType = MsgType.RUN_REPORT;
    }
    int pt = body.indexOf("\nThis message is confidential");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n", -1), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new BaseAddressCityStateField();
    if (name.equals("ADDR2")) return new BaseAddress2Field();
    if (name.equals("UNIT_TIMES")) return new SkipField("Unit Times", true);
    if (name.equals("X_PLACE")) return new BaseCrossPlaceField();
    if (name.equals("GPS")) return new GPSField("\\d+\\.\\d+, -\\d+\\.\\d+", true);
    if (name.equals("PHONE")) return new PhoneField("\\d{9,10}", true);
    if (name.equals("NAME")) return new BaseNameField();
    if (name.equals("TIMES")) return new BaseTimesField();
    return super.getField(name);
  }

  private static final Pattern SHORT_ZIP_PTN = Pattern.compile("(.*\\bTN) \\d{4}");

  private class BaseAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {

      // Fix mistyped zip code
      Matcher match = SHORT_ZIP_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private class BaseAddress2Field extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strAddress.isEmpty()) {
        super.parse(field, data);
        return true;
      }
      else if (checkAddress(data.strAddress) == STATUS_STREET_NAME &&
               checkAddress(field) == STATUS_STREET_NAME) {
        field = data.strAddress + " & " + field;
        data.strAddress = "";
        super.parse(field, data);
        return true;
      } else {
        return false;
      }
    }
  }

  private class BaseCrossPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("@")) {
        data.strCross = append(data.strCross, " / ", field.substring(1).trim());
      }
      else if (field.contains(" / ")) {
        data.strCross = append(data.strCross, " / ", field);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }

  private class BaseNameField extends NameField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(",")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private class BaseTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = getRelativeField(0);
      data.strSupp = append(data.strSupp, "\n", field);
      if (field.startsWith("Completed:") || field.startsWith("Canceled:")) data.msgType = MsgType.RUN_REPORT;
    }
  }
}
