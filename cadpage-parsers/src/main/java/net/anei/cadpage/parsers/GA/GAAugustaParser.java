package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAAugustaParser extends FieldProgramParser {

  public GAAugustaParser() {
    super("AUGUSTA", "GA",
          "CALL ADDR_PFX? ADDRCITY/S6 PLACE X X UNIT! CH END");
  }

  @Override
  public String getFilter() {
    return "dispatch@augustaga.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    int pt = body.indexOf("\n\n___");
    if (pt < 0) return false;
    body = body.substring(0, pt).trim();
    return parseFields(body.split("/"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR_PFX")) return new MyAddressPrefixField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyAddressPrefixField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.endsWith("1") || !getRelativeField(+1).startsWith("2")) return false;
      data.strAddress = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, "/", field);
      data.strAddress = "";
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
}
