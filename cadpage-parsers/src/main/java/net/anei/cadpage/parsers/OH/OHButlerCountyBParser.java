package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHButlerCountyBParser extends FieldProgramParser {

  /**
   * Butler County, OH (B)
   */

  public OHButlerCountyBParser () {
    this("BUTLER COUNTY", "OH");
  }

  public OHButlerCountyBParser (String defCity, String defState) {
    super(defCity, defState,
          "ADDRESS:ADDRCITYST! LOCATION_NAME:PLACE! CROSS_STREETS:X! CALL_TYPE:CALL! ADDITIONAL_CALL_TYPE:CALL/SDS! UNITS:UNIT! CALLER_NAME:NAME! CALLER_PHONE#:PHONE! PROQA_CHIEF_COMPLAINT:LINFO/N? PROQA_SUMMARY:LINFO/N? DETAILS:INFO/N! REPORT#:ID! END");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }

  @Override
  public String getAliasCode() {
    return "OHButlerCountyB";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("<meta ")) {
      int pt = body.indexOf("\nADDRESS:");
      if (pt < 0) return false;
      body = body.substring(pt+1);
    }
    body = body.replace(" REPORT #:", " REPORT#:");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_BRK_PTN = Pattern.compile(" *; *");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_BRK_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
