package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHClermontCountyDParser extends FieldProgramParser {

  public OHClermontCountyDParser() {
    super("CLERMONT COUNTY", "OH", "CALL:CALL! ADDR:ADDR! XST:X CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! INFO:INFO/N+ ST:ST");
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CMalert")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mmaa");
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField(TIME_FMT, true);
    return super.getField(name);
  }
}
