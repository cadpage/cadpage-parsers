package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHPrebleCountyBParser extends FieldProgramParser {

  public OHPrebleCountyBParser() {
    super("PREBLE COUNTY", "OH", 
          "CALL:CODE! PLACE:PLACE ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! INFO:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "PREBLESHERIFF@swohio.twcbc.com";
  }
  
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mmaa");
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField(TIME_FMT, true);
    return super.getField(name);
  }
  
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      data.strCall = convertCodes(field, CALL_CODES); 
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "4",  "MVA",
      "16", "Dead Body",
      "28", "Fire",
      "29", "EMS Response",
      "C4", "MVA"
  });

}
