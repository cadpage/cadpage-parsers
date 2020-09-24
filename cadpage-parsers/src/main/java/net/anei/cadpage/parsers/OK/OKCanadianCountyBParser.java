package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OKCanadianCountyBParser extends FieldProgramParser {
  
  public OKCanadianCountyBParser() {
    super("CANADIAN COUNTY", "OK", 
          "( SELECT/1 ADDR CITY ST/Y! END " + 
          "| CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY_ST! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:INFO! END )");
  }

  @Override
  public String getFilter() {
    return "centralsquare@cityofmustang.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    if (subject.equals("no subject") && body.startsWith("Body:")) {
      body = body.substring(5).trim();
      setSelectValue("2");
      return super.parseMsg(body, data);
    } else {
      data.strCall = subject;
      setSelectValue("1");
      return parseFields(body.split(","), data);
    }
  }
  
  @Override
  public String getProgram() {
    return "CALL? " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY_ST")) return new MyCityStateField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyCityStateField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        data.strState = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      data.strCity = field;
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
    
  }
}
