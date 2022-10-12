package net.anei.cadpage.parsers.MI;


import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIInghamCountyBParser extends FieldProgramParser{

  public MIInghamCountyBParser() {
    this("INGHAM COUNTY", "MI");
  }

  public MIInghamCountyBParser(String defCity, String defState) {
    super(defCity, defState, 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! APT:APT! CITY:CITY! LAT:GPS1! LON:GPS2 ID:ID! TIME:DATETIME! UNIT:UNIT! INFO:INFO+", FLDPROG_IGNORE_CASE);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d|", true);
    return super.getField(name);
  }
  
  private class MyGPSField extends GPSField {
    
    public MyGPSField(int type) {
      super(type);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.length() != 8 || !NUMERIC.matcher(field).matches()) abort();
      super.parse(field.substring(0,2) +'.' + field.substring(2), data);
    }
  }
}
