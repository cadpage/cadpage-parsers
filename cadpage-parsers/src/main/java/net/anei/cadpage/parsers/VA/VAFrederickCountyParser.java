package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAFrederickCountyParser extends DispatchOSSIParser {

  public VAFrederickCountyParser() {
    super("FREDERICK COUNTY", "VA",
          "ADDR CALL! ( ID_UNIT! | X ( ID_UNIT! | X/Z ID_UNIT! | X/Z X/Z ID_UNIT | X? ) | PLACE ID_UNIT! | PLACE X/Z ID_UNIT! | PLACE X/Z X/Z ID_UNIT! | ) ID_UNIT? INFO+");
  }

  @Override
  public String getFilter() {
    return "CAD@co.frederick.va.us,CAD@psb.net,cad@fcva.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }



  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID_UNIT")) return new MyIdUnitField();
    return super.getField(name);
  }

  private static final Pattern CROSS_PTN = Pattern.compile("\\b(?:ACCESS|INTERSTATE|RAMP|XOVER)\\b", Pattern.CASE_INSENSITIVE);
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (CROSS_PTN.matcher(field).find() || isValidCrossStreet(field)) {
        super.parse(field, data);
        return true;
      } else {
        return super.checkParse(field, data);
      }
    }
  }

  // The call ID or Unit can come in either order, and they are a critical decision field, so we
  // will have one processor that handles them both :(

  private static Pattern ID_UNIT_PTN = Pattern.compile("(\\d{8})|((?:\\b(?:ALS|DFM|FW|STAF|[A-Z]+\\d+)\\b,?)+)");

  private class MyIdUnitField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ID_UNIT_PTN.matcher(field);
      if (!match.matches()) return false;
      String id = match.group(1);
      if (id != null) {
        if (!data.strCallId.isEmpty()) return false;
        data.strCallId = id;
        return true;
      } else {
        if (!data.strUnit.isEmpty()) return false;
        data.strUnit = field;
        return true;
      }
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ID UNIT";
    }
  }
}