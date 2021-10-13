package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA86Parser extends FieldProgramParser {

  public DispatchA86Parser(String defCity, String defState) {
    super(defCity, defState,
          "CALL:CALL! Desc:CALL2? PLACE:ADDRCITY! ID:ID! INFO:INFO! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD DISPATCH")) return false;
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new BaseCallField();
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    return super.getField(name);
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strCall)) return;
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private static final Pattern ADDR_ID_GPS_PTN = Pattern.compile("(.*?)(?: \\[(\\d+)\\])?(?: *\\(([^()]+)\\))?");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_ID_GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      field = match.group(1).trim();
      data.strBox = getOptGroup(match.group(2));
      String gps = match.group(3);
      if (gps != null) setGPSLoc(gps, data);
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " BOX GPS";
    }
  }
}