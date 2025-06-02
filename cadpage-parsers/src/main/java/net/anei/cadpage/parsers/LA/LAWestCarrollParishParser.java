package net.anei.cadpage.parsers.LA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LAWestCarrollParishParser extends FieldProgramParser {

  public LAWestCarrollParishParser() {
    super(CITY_CODES, "WEST CARROLL PARISH", "LA",
          "Unit:UNIT! Date/Time:DATETIME/d! Incident:ID! Type:CALL! Subtype:CALL/SDS? Address1:ADDR! Address2:EMPTY! Community:GPS! City:CITY! Complainant:NAME! Contact_Number:PHONE! END");
  }

  @Override
  public String getFilter() {
    return "monitor@emergencycallworx.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Unit Dispatched Notification")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("PHONE")) return new MyPhoneField();
    return super.getField(name);
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Unknown")) return;
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "OAKGROVE",     "OAK GROVE",
      "WESTCPA",      "WEST CARROLL PARISH"
  });
}
