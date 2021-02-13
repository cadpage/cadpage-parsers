package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA71Parser extends FieldProgramParser {

  public DispatchA71Parser(String defCity, String defState) {
    super(defCity, defState,
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! APT:APT? CITY:CITY! XY:GPS? ID:ID! PRI:PRI? DATE:DATE! TIME:TIME! UNIT:UNIT? X:X? INFO:INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d?:\\d\\d?", true);
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_SECTOR_PTN = Pattern.compile("(.*?)[- ]+([NSEW]{1,2} SECTOR|SEC [NSEW]{1,2})", Pattern.CASE_INSENSITIVE);

  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_SECTOR_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strMap = match.group(2);
      }
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " MAP";
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("@ *", "/").replace('@', '/');
      super.parse(field, data);
    }
  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("COMMENT:")) {
        int pt = field.indexOf('|', 8);
        pt = (pt >= 0 ? pt+1 : 8);
        field = field.substring(pt).trim();
      }
      super.parse(field, data);
    }
  }
}
