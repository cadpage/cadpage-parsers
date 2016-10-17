package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
/**
 * Village, TX
 */
public class TXVillageParser extends FieldProgramParser {
  
  public TXVillageParser() {
    super("VILLAGE", "TX",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CROSS:X! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! MAP:MAP! Key_Map:MAP! UNIT:UNIT! INFO:INFO+");
  }
  
  public String getFilter() {
    return "contact@villagefire.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{4}", true);
    if (name.equals("DATE")) return new DateField("\\d{1,2}/\\d{1,2}/\\d{4}|", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d|", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('_', ' ').trim();
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override 
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
