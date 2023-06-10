package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA86Parser extends FieldProgramParser {

  public DispatchA86Parser(String defCity, String defState) {
    super(defCity, defState,
          "CALL:CALL! Desc:CALL2? PLACE:ADDRCITY! ID:ID? INFO:INFO! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD DISPATCH") && !subject.equals("CAD INCIDENT")) return false;
    int pt = body.indexOf("\nURL:");
    if (pt >= 0) body = body.substring(0,pt).trim();
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
  private static final Pattern COMMA_PTN = Pattern.compile(" *, *");
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("[- A-Za-z]*");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "INTERSECTION:");
      Matcher match = ADDR_ID_GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      field = match.group(1).trim();
      data.strBox = getOptGroup(match.group(2));
      String gps = match.group(3);
      if (gps != null) setGPSLoc(gps, data);

      String[] parts = COMMA_PTN.split(field);
      switch (parts.length) {
      case 1:
        break;

      case 2:
        if (ADDR_CITY_PTN.matcher(parts[1]).matches()) {
          field = parts[0];
          data.strCity = parts[1];
        } else {
          data.strPlace = parts[0];
          field = parts[1];
        }
        break;

      case 3:
        for (int jj = 0; jj < parts.length-2; jj++) {
          data.strPlace = append(data.strPlace, ",", parts[jj]);
        }
        field = parts[parts.length-2];
        data.strCity = parts[parts.length-1];
      }
      parseAddress(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames() + " BOX GPS";
    }
  }
}
