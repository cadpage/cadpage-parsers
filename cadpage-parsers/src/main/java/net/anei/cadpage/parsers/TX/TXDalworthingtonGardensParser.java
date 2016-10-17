package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXDalworthingtonGardensParser extends FieldProgramParser {
  
  public TXDalworthingtonGardensParser() {
    super(CITY_CODES, "DALWORTHINGTON GARDENS", "TX", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! DATE:DATE! TIME:TIME! MAP:MAP UNIT:UNIT INFO:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@cityofdwg.net";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!super.parseFields(body.split("\n"), data)) return false;
    if (data.strAddress.length() == 0) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("(\\d\\d)(\\d\\d)|\\d\\d:\\d\\d(?::\\d\\d)?");
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      if (field.length() == 4) {
        field = match.group(1) + ':' + match.group(2);
      }
      super.parse(field, data);
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NONE")) return;
      super.parse(field, data);
    }
  }
  
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "DWG", "DALWORTHINGTON GARDENS"
  });

}
