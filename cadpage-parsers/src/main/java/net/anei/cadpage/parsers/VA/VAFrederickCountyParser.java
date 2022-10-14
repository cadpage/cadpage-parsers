package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAFrederickCountyParser extends DispatchOSSIParser {

  public VAFrederickCountyParser() {
    super("FREDERICK COUNTY", "VA",
          "ADDR CALL! ( PLACE X/Z X/Z ID! | ID! | X ( ID! | X/Z ID! | X? ) | PLACE X/Z ID! | PLACE ID! | ) UNIT? INFO+");
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


  private static final Pattern UNIT_PTN = Pattern.compile("(?:\\b[A-Z]+\\d+\\b,?)+");

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new UnitField(UNIT_PTN, true);
    return super.getField(name);
  }

  private static final Pattern CROSS_PTN = Pattern.compile("\\b(?:ACCESS|RAMP|XOVER)\\b", Pattern.CASE_INSENSITIVE);
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {

      if (checkUnit(field, data)) return true;

      if (CROSS_PTN.matcher(field).find() || isValidCrossStreet(field)) {
        super.parse(field, data);
        return true;
      } else {
        return false;
      }
    }

    @Override
    public void parse(String field, Data data) {
      if (checkUnit(field, data)) return;
      super.parse(field, data);
    }

    private boolean checkUnit(String field, Data data) {
      if (UNIT_PTN.matcher(field).matches()) {
        data.strUnit = field;
        return true;
      } else {
        return false;
      }
    }

    @Override
    public String getFieldNames() {
      return "X UNIT?";
    }
  }
}