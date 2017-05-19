package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHMercerCountyBParser extends FieldProgramParser {
  
  public OHMercerCountyBParser() {
    super("MERCER COUNTY", "OH", 
          "CODE! PLACE:PLACE ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! INFO:INFO");
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return OHMercerCountyParser.adjustMapAddr(addr);
  }
  
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mmaa");
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField(TIME_FMT, true);
    return super.getField(name);
  }
  
  private class MyCodeField extends Field {
    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      data.strCall = convertCodes(field, OHMercerCountyParser.CALL_CODES);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" BETWEEN ");
      if (pt >= 0) {
        data.strCross = field.substring(pt+9).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field =  field.replace(' ', '-');
      super.parse(field, data);
    }
  }
}
