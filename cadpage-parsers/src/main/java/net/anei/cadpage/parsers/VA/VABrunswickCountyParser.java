package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class VABrunswickCountyParser extends DispatchOSSIParser {


  public VABrunswickCountyParser() {
    super(CITY_CODES, "BRUNSWICK COUNTY", "VA",
          "( CANCEL ADDR CITY! INFO/N+ " +
          "| FYI? ( ID DATETIME CALL PLACE? ADDR/Z CITY/Y! X/Z+? ( UNIT END | END ) " +
                 "| DATETIME ID CALL UNIT PLACE? ADDR/Z CITY/Y! X/+? NAME PHONE " +
                 "| CALL ADDR CITY/Y! INFO/N+ ) )");
    setupCityValues(CITY_CODES);
  }

  @Override
  public String getFilter() {
    return "CAD@brunswickso.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" +  body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{5,}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("SRC")) return new SourceField("DOLPHIN VOLUNTEER FIRE DEPARME|", true);
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:\\d{1,4}[A-Z]*|[A-Z]{4}|[A-Z]{1,2}\\d+|FRSTRY)\\b *)+");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("REF#")) return;
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALBE",   "ALBERTA",
      "BLAC",   "BLACKSTONE",
      "BROD",   "BRODNAX",
      "DOLP",   "DOLPHIN",
      "DUND",   "DUNDAS",
      "EBON",   "EBONY",
      "EMPO",   "EMPORIA",
      "FREE",   "FREEMAN",
      "GASB",   "GASBURG",
      "LA C",   "LACROSSE",
      "LAWR",   "LAWRENCEVILLE",
      "RAWL",   "RAWLINGS",
      "VALE",   "VALENTINES",
      "WARF",   "WARFIELD",
      "WHIT",   "WHITE PLAINS"
  });
}
