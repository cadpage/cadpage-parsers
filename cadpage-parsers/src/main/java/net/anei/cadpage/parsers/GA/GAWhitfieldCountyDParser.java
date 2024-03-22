package net.anei.cadpage.parsers.GA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAWhitfieldCountyDParser extends DispatchOSSIParser {

  public GAWhitfieldCountyDParser() {
    super(CITY_CODES, "WHITFIELD COUNTY", "GA",
          "( UNIT ENROUTE/R ADDR CITY ( CODE! | CALL ) END " +
          "| ADDR INFO+? CALL CODE/Z! END " +
          ")");
  }

  @Override
  public String getFilter() {
    return "CAD@whitfieldcountyga.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Text Message")) return false;
    if (body.contains(",Enroute,")) body = body.replace(',', ';');
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body,  data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ENROUTE")) return new CallField("Enroute", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CODE")) return new CodeField("[A-Z]\\d{1,2}[A-Z]?", true);
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (checkAddress(field) == STATUS_STREET_NAME) {
        data.strCross = append(data.strCross, " / ", field);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }

  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "DALT", "DALTON"
  });
}
