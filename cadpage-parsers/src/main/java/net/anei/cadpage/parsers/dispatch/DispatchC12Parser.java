package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchC12Parser extends FieldProgramParser {

  public DispatchC12Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "Incident:EMPTY! Nature:CALL! Address:ADDRCITYST/S! Cross_Streets:X? Priority:PRI? Coordinates:GPS? ID:ID! Units:UNIT! Comments:EMPTY! INFO/N+");
  }

  private static final Pattern SRC_PTN = Pattern.compile("(.*?) +CAD Incident");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SRC_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    body = body.replace("\n\t", "\n").replace(": : ", ": ");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new BaseAddressCityStateField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private class BaseAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt < 0) abort();
        data.strPlace = field.substring(pt+1,field.length()-1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("-+|createdAt:.*");

  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "message:");
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
}
