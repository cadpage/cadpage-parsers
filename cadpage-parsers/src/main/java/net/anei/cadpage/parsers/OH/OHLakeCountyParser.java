package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHLakeCountyParser extends FieldProgramParser {
  
  public OHLakeCountyParser() {
    super(CITY_CODES, "LAKE COUNTY", "OH",
           "Location:ADDR/S? APT:APT? Cross_Streets:X! TYPE_CODE:CALL! Sub_Type:CALL! TIME:TIME! Comments:INFO");
  }
  
  @Override
  public String getFilter() {
    return "ingr.com@archwireless.net,ingr.com@usamobility.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.startsWith("&")) {
      data.strPlace = append(data.strAddress.substring(1).trim(), " - ", data.strPlace);
      data.strAddress = "";
    }
    if (data.strAddress.length() == 0) {
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "PLACE " + super.getProgram();
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CO", "CONCORD TWP",
      "FH", "FAIRPORT HARBOR",
      "LE", "LEROY TWP",
      "MA", "MADISON",
      "MV", "MADISON",
      "NP", "NORTH PERRY",
      "PA", "PAINSVILLE",
      "PE", "PERRY",
      "PC", "PAINSVILLE",
      "PV", "PERRY",
  });
}
