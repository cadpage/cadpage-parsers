package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * San Diego County, CA
 */
public class CASanDiegoCountyCParser extends FieldProgramParser {  
  public CASanDiegoCountyCParser() {
    super("SAN DIEGO COUNTY", "CA",
           "TIME ID CALL CH ADDR! ( T:MAP! F:MAP/D | F:MAP! T:MAP/D ) X:X! PLACE PFP:BOX ALM:PRI DIST:MAP2! UNITS:UNIT");
  }

  @Override
  public String getFilter() {
    return "FCC@SDFD.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" DIST:", "\\DIST:").replace(" F:", "\\F:");
    String[] field = body.split("\\\\ *");
    return parseFields(field, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MAP2")) return new MyMap2Field();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private class MyMap2Field extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(field, "-", data.strMap);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
