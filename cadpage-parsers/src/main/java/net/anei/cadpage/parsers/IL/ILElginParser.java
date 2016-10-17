package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ILElginParser extends FieldProgramParser {
  public ILElginParser() {
    super("ELGIN", "IL",
        "CALL! ADDRCITY/S6Xa PLACE X PLACE2 UNIT! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@Cityofelgin.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
  
    return parseFields(body.split(":"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new CodeField("[A-Z0-9]+", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PLACE2")) return new PlaceField("(.*?) *\\bUnits", true);
    if (name.equals("UNIT")) return new UnitField("(.*?) *\\bNarrative", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&').toUpperCase();
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found"))
        data.strCross = "";
      else
        data.strCross = field;
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, ":", field);
    }
  }
}
