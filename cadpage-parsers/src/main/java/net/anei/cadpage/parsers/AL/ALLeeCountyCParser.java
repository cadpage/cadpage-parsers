package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALLeeCountyCParser extends FieldProgramParser {

  public ALLeeCountyCParser() {
    super("LEE COUNTY", "AL",
          "Call_Type:CALL! Caller_Name:NAME! Call_Back_Number:PHONE! Address_of_Call:ADDRCITYST! Notes:INFO! INFO/N+ NOM_Agent:SKIP!");
  }

  @Override
  public String getFilter() {
    return "messages@tascalls.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("- -") || field.equals("N/A")) return;
      super.parse(field, data);
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("- -") || field.equals("N/A")) return;
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("- -") || field.equals("N/A")) return;
      super.parse(field, data);
    }
  }
}
