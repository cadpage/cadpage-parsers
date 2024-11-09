package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYHickmanCountyParser extends FieldProgramParser {

  public KYHickmanCountyParser() {
    super("HICKMAN COUNTY", "KY",
          "ID PRI CALL PLACE+? ADDRCITYST/Z UNIT EMPTY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "paging@10-8systems.com";
  }

  private static final Pattern DELIM = Pattern.compile(" /(?= )");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("NEW DISPATCH")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("INC +(.*)", true);
    if (name.equals("PRI")) return new PriorityField("Pri (\\d)", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (getRelativeField(+1).startsWith(field)) return;
      super.parse(field, data);
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ", USA");
      super.parse(field, data);
    }
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {3,}");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(", USA");
      if (pt >= 0) field = field.substring(pt+5).trim();
      field = MSPACE_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }
}
