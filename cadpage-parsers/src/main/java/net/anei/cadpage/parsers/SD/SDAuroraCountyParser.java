package net.anei.cadpage.parsers.SD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SDAuroraCountyParser extends FieldProgramParser {

  public SDAuroraCountyParser() {
    this("AURORA COUNTY", "SD");
  }

  SDAuroraCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "Assigned_Unit:UNIT! Date/Time:DATETIME! Location:ADDRCITYST! Incident:CALL! Phone_#:PHONE END");
  }

  @Override
  public String getAliasCode() {
    return "SDAuroraCounty";
  }

  @Override
  public String getFilter() {
    return "ithelp@cityofmitchell.org,zuercherled@mitchelldps.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("ALERT")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("PHONE")) return new MyPhoneField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

}
