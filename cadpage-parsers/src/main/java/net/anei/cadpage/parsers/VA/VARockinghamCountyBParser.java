package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class VARockinghamCountyBParser extends DispatchH05Parser {

  public VARockinghamCountyBParser() {
    super("ROCKINGHAM COUNTY", "VA",
          "( Call:CALL! Address:ADDRCITY/S6! X_Street:X! Incident_#:ID! Tac:CH! Priority:PRI! Units:UNIT! Fire_Box:BOX! Lat:GPS1! Lon:GPS2! CAD_#:ID/L! Unit_Times:EMPTY! TIMES+ " +
          "| INCIDENT_#:ID! DATE/TIME:DATETIME CALL_TYPE:CALL! ( TAC_Channel:CH! | Ops_Channel:CH! ) ADDR:ADDRCITY/S6! ( X_STREET:X! LAT:GPS1! LON:GPS2! | ) UNITS:UNIT! UNIT_EXT+? ( NARRATIVE:EMPTY! INFO_BLK/N+ | ) ( UNIT_TIMES:EMPTY! TIMES+ CAD_#:SKIP? | INCIDENT_TIMES:EMPTY! TIMES+ NOTES:EMPTY! INFO_BLK/N+ ) END " +
          ")");
  }

  @Override
  public String getFilter() {
    return "caddmsmailbox@hrecc.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    if (subject.equals("C30") || subject.equals("!")) {
      if (!subject.equals("!")) data.strSource = subject;
      body = body.replace("<br>", "\n");
      return parseFields(body.split("\n"), data);
    }

    else {
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT_EXT")) return new MyUnitExtField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '/');
      super.parse(field, data);
    }
  }

  private class MyUnitExtField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.isEmpty()) return true;
      if (!field.startsWith(",")) return false;
      data.strUnit = data.strUnit + field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
