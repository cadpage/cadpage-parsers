package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOTexasCountyParser extends FieldProgramParser {

  public MOTexasCountyParser() {
    super("TEXAS COUNTY", "MO",
          "Event_Number:ID! Category:CALL! CALL/S+ Sub_Category:CALL/S! CALL/S+ Status_Code:SKIP! Address:ADDRCITY! INCIDENT_NOTES%EMPTY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "texascounty911@omnigo.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      p.getLastOptional(',');
      data.strCity = p.getLastOptional("  ");
      parseAddress(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Note Data")) return;
      super.parse(field, data);
    }
  }
}
