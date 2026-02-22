package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOMississippiCountyParser extends FieldProgramParser {

  public MOMississippiCountyParser() {
    super("MISSISSIPPI COUNTY", "MO",
          "( Category:CALL! Address:ADDR! END " +
          "| ID CALL ADDR1 ADDR1 ADDR2 CITY NAME NAME/S! PHONE PLACE EMPTY INFO/N+ " +
          ")");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID"))  return new IdField("\\d{4}-\\d{5}", true);
    if (name.equals("ADDR1")) return new MyAddressField(false);
    if (name.equals("ADDR2")) return new MyAddressField(true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {

    boolean complete;

    public MyAddressField(boolean complete) {
      this.complete = complete;
    }

    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, " ", field);
      if (complete) {
        field = data.strAddress;
        data.strAddress = "";
        super.parse(field, data);
      }
    }
  }
}
