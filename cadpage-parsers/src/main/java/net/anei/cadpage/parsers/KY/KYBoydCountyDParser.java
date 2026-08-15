package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYBoydCountyDParser extends FieldProgramParser {

  public KYBoydCountyDParser() {
    super("BOYD COUNTY", "KY",
          "LOG_NUMBER:ID! COMPANY:SKIP? NAME_OF_FACILITY:PLACE! ( ADDRESS:ADDR! CITY/STATE/ZIP:CITYSTZIP! | ) ROOM_NUMBER:APT! " +
                "( PRIORITY:PRI! | PRIORITY_LEVEL:PRI! ) TRANSPORT_TYPE:CALL/SDS? " +
                "( REASON:CALL/D! | REASON_FOR_TRANSPORT:CALL/D! ) CALL_BACK_NUMBER:PHONE! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "no-reply@base44-apps.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;

    int pt = body.indexOf("<img src=");
    if (pt >= 0) body = body.substring(0,pt).trim();

    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITYSTZIP")) return new MyCityStateZipField();
    return super.getField(name);
  }

  private class MyCityStateZipField extends Field {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.get(',');
      data.strState = p.get(',');
      if (data.strCity.isEmpty()) data.strCity = p.get();
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }

  }
}
