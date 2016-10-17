package net.anei.cadpage.parsers.dispatch;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class DispatchA59Parser extends FieldProgramParser {
  
  public DispatchA59Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }
  
  public DispatchA59Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
           "EVENT:ID! TIME:DATETIME! TYPE:CALL! LOC:ADDR/S! TXT:INFO!");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if(!subject.equals("CAD")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String  name) { 
	  if (name.equals("ID")) return new IdField("\\d{4}",true);
	  if (name.equals("DATETIME")) return new DateTimeField("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}", true);
    return super.getField(name);
  }
}
