package net.anei.cadpage.parsers.NJ;

import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJPassaicCountyParser extends FieldProgramParser {

  public NJPassaicCountyParser() {
    super("PASSAIC COUNTY", "NJ", 
          "CAD#:ID! Time:TIME! Address:ADDR! Cross:X! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "FireDispatch@cityofpassaicnj.gov";
  }
  
  @Override protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Fire Page / ");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new DateTimeField(new SimpleDateFormat("hh:mm:ss aa"), true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" - ", " / ");
      field = stripFieldStart(field, "-");
      field = stripFieldEnd(field, "-");
      super.parse(field, data);
    }
  }
}
