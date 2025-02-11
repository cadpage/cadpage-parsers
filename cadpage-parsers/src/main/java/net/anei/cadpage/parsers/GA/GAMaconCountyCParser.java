package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAMaconCountyCParser extends FieldProgramParser {

  public GAMaconCountyCParser() {
    super("MACON COUNTY", "GA",
          "ID? ADDRCITY ( Caller:NAME! Phone#:PHONE! Incident_#:ID! Narrative:INFO! " +
                       "| DATETIME! UNITS:UNIT! NARRATIVE:INFO! " +
                       ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "Dispatch@maconbibb.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    body = stripMarker(body, "This email ");
    body = stripMarker(body, "Message sent ");
    return super.parseFields(body.split("\n+"), data);
  }

  private String stripMarker(String body, String marker) {
    if (body.startsWith(marker)) {
      int pt = body.indexOf('\n');
      if (pt >= 0) body = body.substring(pt+1).trim();
    }
    return body;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}\\b.*", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("(\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d)\\b.*", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (!field.contains("@")) field = field.replace(" at ", " @ ");
      Parser p = new Parser(field);
      data.strCall = p.getOptional('@');
      if (data.strCall.isEmpty()) abort();
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, p.get(','), data);
      data.strCity = p.get(',');
      data.strPlace = p.get();
    }

    @Override
    public String getFieldNames() {
      return "CALL ADDR APT CITY PLACE";
    }

  }
}
