package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOClayCountyEParser extends FieldProgramParser {

  public MOClayCountyEParser() {
    super("CLAY COUNTY", "MO",
          "ID CALL CALL2/L+? NONE? ADDR_PFX+? ADDRCITYST EMPTY! EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "zuercher@gladstone.mo.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Call")) return false;
    return parseFields(body.split("/", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[A-Z]{1,2}\\d{6}-\\d{3}", true);
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("NONE")) return new SkipField("None");
    if (name.equals("ADDR_PFX")) return new MyAddressPrefixField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("None")) return false;
      if (getRelativeField(+1).isEmpty()) return false;
      if (isValidCrossStreet(field)) return false;
      data.strCall = append(data.strCall, "/", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyAddressPrefixField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (getRelativeField(+1).isEmpty()) return false;
      data.strAddress = append(data.strAddress, " & ", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, " & ", field);
      data.strAddress = "";
      super.parse(field, data);
    }
  }

}
