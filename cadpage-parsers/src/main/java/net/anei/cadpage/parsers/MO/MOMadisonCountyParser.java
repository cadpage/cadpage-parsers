package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MOMadisonCountyParser extends FieldProgramParser {

  public MOMadisonCountyParser() {
    this("MADISON COUNTY", "MO");
  }

  public MOMadisonCountyParser(String defCity, String defState) {
    super(defCity, defState,
          "Event_#:ID! Call_Date:SKIP! Address:ADDRCITYST! City:CITY! Cross_Street:X! Phone:PHONE! Police_Type:CALL! Fire_Type:CALL/SLS! EMS_Type:CALL/SLS! " +
              "EMS_Report_#:ID/L! Person_Name:NAME! Business_Name:PLACE! Unit:UNIT! Notes:EMPTY! INFO/N+");
  }

  @Override
  public String getAliasCode() {
    return "MOMadisonCounty";
  }

  @Override
  public String getFilter() {
    return "noreply@valorsystems.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Automated Message")) return false;
    body = body.replace(" MO, ", ", MO ");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      super.parse(field, data);
    }
  }
}
