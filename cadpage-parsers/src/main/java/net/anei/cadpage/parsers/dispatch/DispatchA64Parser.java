package net.anei.cadpage.parsers.dispatch;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA64Parser extends FieldProgramParser{

  public DispatchA64Parser(String defCity, String defState) {
    super(defCity, defState,
          "Call_Type:CALL City:CITY Address:ADDRCALL! Apartment:APT END");
  }

  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.trim().equals("Dispatch Alert")) return false;
    if (body.contains("\n")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCALL")) return new BaseAddressCallField();
    return super.getField(name);
  }

  private class BaseAddressCallField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      int semicolonPosition = field.indexOf(";"); // in 2 of the sample messages during an "ASSIST OTHER AGENCY" call a more specific secondary call was added to the end separated by a semicolon.
      if (semicolonPosition >= 0) {
        data.strCall = append(data.strCall, " / ", field.substring(semicolonPosition + 1)).trim();
        field =field.substring(0, semicolonPosition).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CALL";
    }
  }
}