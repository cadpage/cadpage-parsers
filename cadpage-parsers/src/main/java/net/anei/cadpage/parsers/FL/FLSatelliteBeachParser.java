package net.anei.cadpage.parsers.FL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLSatelliteBeachParser extends FieldProgramParser {

  public FLSatelliteBeachParser() {
    super("SATELLITE BEACH", "FL", 
          "Address:ADDR! City:CITY! Cross:X! Place:PLACE! Call:CODE! Incident_#:ID! Nature:CALL! Unit:UNIT! Info:INFO! Nature_Of_Call:CALL? Nature_Of_Call:SKIP? Dispatcher:SKIP! GPS:GPS!");
  }
  
  @Override
  public String getFilter() {
    return "newworld@satellitebeach.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=Call:)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Notification")) return false;
    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strCall)) return;
      data.strCall = append(data.strCall, " - ", field);
    }
  }

}
