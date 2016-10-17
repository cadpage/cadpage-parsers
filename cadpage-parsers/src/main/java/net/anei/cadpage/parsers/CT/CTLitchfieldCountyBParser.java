package net.anei.cadpage.parsers.CT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTLitchfieldCountyBParser extends FieldProgramParser {
  
  public CTLitchfieldCountyBParser() {
    super("LITCHFIELD COUNTY", "CT", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR/S6! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "winchesterpddispatch@yahoo.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField(TIME_FMT, true);
    return super.getField(name);
  }
}
