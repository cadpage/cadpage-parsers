package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchC05Parser extends FieldProgramParser {

  public DispatchC05Parser(String defCity, String defState) {
    super(defCity, defState,
          "ID PRI CALL PLACE+? ADDRCITYST/Z UNIT EMPTY! INFO/N+");
  }

  private static final Pattern DELIM = Pattern.compile(" /(?= )");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("NEW DISPATCH")) return false;
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("INC +(.*)", true);
    if (name.equals("PRI")) return new PriorityField("Pri (\\d)", true);
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("ADDRCITYST")) return new BaseAddressCityStateField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (getRelativeField(+1).startsWith(field)) return;
      super.parse(field, data);
    }
  }

  private class BaseAddressCityStateField extends AddressCityStateField {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ", USA");
      super.parse(field, data);
    }
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {3,}");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(", USA");
      if (pt >= 0) field = field.substring(pt+5).trim();
      field = MSPACE_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }
}
