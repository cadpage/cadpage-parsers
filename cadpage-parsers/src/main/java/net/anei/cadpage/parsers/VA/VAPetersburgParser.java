package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAPetersburgParser extends DispatchOSSIParser {

  public VAPetersburgParser() {
    super(CITY_CODES, "PETERSBURG", "VA",
          "( CANCEL ADDR CITY! | FYI CALL PRI? ADDR! CITY? ( X X? | ) ) INFO/N+");
    setupSpecialStreets("BOULEVARD");
    removeWords("NEW");
  }

  @Override
  public String getFilter() {
    return "CAD@petersburg-police.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\nThis e-mail");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
    "PTBG", "PETERSBURG"
  });
}
