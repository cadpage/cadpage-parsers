package net.anei.cadpage.parsers.MO;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOWashingtonCityParser extends FieldProgramParser {

  public MOWashingtonCityParser() {
    super("WASHINGTON", "MO", 
          "TIME CALL ADDR ( OUT OUT | PLACE? ( BOX X | X ) X ) INFO/L+? UNIT!");
  }
  
  @Override
  public String getLocName() {
    return "Washington City, MO";
  }
  
  public boolean parseMsg(String subject, String body, Data data) {
    String[] fields = body.split("/");
    
    //if there aren't enough fields to satisfy program string, parse general alert
    if (fields.length < 6) {
      data.parseGeneralAlert(this, body);
      return true;
    }
    
    //otherwise just parse
    return parseFields(body.split("/"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d{2}:\\d{2}:\\d{2}", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("OUT")) return new CrossField("OUTSIDE DISTRICT", true);
    if (name.equals("BOX")) return new BoxField("\\d{4}", true);
    if (name.equals("UNIT")) return new UnitField("([A-Z]?\\d+(?: [A-Z]?\\d+)*)", true);
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      String call = CALL_CODES.getProperty(field);
      if (call != null) {
        data.strCode = field;
        data.strCall = call;
      } else {
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "F",       "First Alarm",
      "HMF",     "Hazmat First",
      "LT",      "Life Threatening",
      "MJR",     "Major Rescue",
      "MR",      "Minor Rescue",
      "MUA",     "Move Up Ambulance",
      "MUP",     "Move up Pumper",
      "RA",      "Routine Ambulance",
      "RWF",     "River Water First",
      "RWR",     "River Water Rescue",
      "S",       "Still Alarm",
      "SB",      "Stand By",
      "SP",      "Special Alarm",
      "SPECIAL", "Special Alarm",
      "STILL",   "Still Alarm",
      "VA",      "Vehicle Accident",
      "VR",      "Vehicle Rescue",
      "WDS",     "Weeds",

  });

}
