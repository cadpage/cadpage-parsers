package net.anei.cadpage.parsers.ZNZ;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZNZAucklandParser extends FieldProgramParser {

  public ZNZAucklandParser() {
    super(ZNZNewZealandParser.CITY_LIST, "AUCKLAND", "", CountryCode.NZ, 
          "ADDR:ADDR/SP! BOX:BOX!  CALL:CALL! CITY:SKIP! CO:SKIP! CODE:CODE! DATE:DATE! GPS:GPS! ID:ID! INFO:INFO! PRI:PRI! SRC:SRC! TIME:TIME! UNIT:UNIT! INFO/N+ XSTR:X! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "auck.ops1@fire.org.nz";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }
  
  private static final DateFormat DATE_FMT = new SimpleDateFormat("dd/MM/yyyy");
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d/\\d{4}", DATE_FMT, true);
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  private class MyTimeField extends TimeField {
    public MyTimeField() {
      super("\\d\\d?:\\d\\d [ap]\\.?m\\.?", TIME_FMT, true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace(".", "");
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_EMPTY_ADDR_OK;
  }
}
