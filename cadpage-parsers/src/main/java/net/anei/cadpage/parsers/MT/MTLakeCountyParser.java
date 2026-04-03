package net.anei.cadpage.parsers.MT;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTLakeCountyParser extends FieldProgramParser {

  public MTLakeCountyParser() {
    this("LAKE COUNTY", "MT");
  }

  MTLakeCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "Address:ADDRCITYST! Address_Name:PLACE! Location_Details:PLACE! CFS_Number:ID! Incident_Type:CALL! Caller:NAME! Dispatcher:SKIP! " +
              "Call_Time:DATETIME! Call_Location:SKIP! Responding_Units:UNIT! Call_Notes:INFO! Message:EMPTY! CFS_Latitude:GPS1! CFS_Longitude:GPS2! END");
  }

  @Override
  public String getFilter() {
    return "no-reply@csprosuite.centralsquarecloudgov.com";
  }

  @Override
  public String getAliasCode() {
    return "MTLakeCounty";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
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

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = field;
    }
  }
}
