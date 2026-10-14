package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INAllenCountyBParser extends FieldProgramParser {

  public INAllenCountyBParser() {
    super("ALLEN COUNTY", "IN",
          "CALL ADDR! INFO/N+ Cross_Streets:EMPTY! X END");
  }

  @Override
  public String getFilter() {
    return "911@allencounty.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("DISPATCH:")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strMap = p.getLastOptional(" Zone:");
      if (data.strMap.isEmpty()) abort();
      data.strPlace = p.getLastOptional(';');
      field = p.get();
      if (field.isEmpty()) abort();
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE MAP";
    }
  }
}
