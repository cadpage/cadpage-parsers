package net.anei.cadpage.parsers.AL;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Madison County, AL
 */

public class ALMadisonCountyParser extends FieldProgramParser {
  
  private static final String CAD_MARKER = "IPS I/Page Notification";
  
  public ALMadisonCountyParser() {
    super(CITY_TABLE, "MADISON COUNTY", "AL",
           "EVENT:ID? Loc:ADDR! EVT#:ID TYPE:CALL TIME:TIME GRID_ID:MAP");
  }
  
  @Override
  public String getFilter() {
    return "Madco911,rescue1-bounces@rescuesquad.net,cad.page@madco9-1-1.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (body.startsWith("/ ")) body = body.substring(2).trim();
    do {
      if (subject.contains(CAD_MARKER)) break;
      
      if (body.startsWith(CAD_MARKER + " / ")) {
        body = body.substring(CAD_MARKER.length()+3);
        break;
      }
      
      if (body.startsWith("Loc:")) break;
      
      return false;
    } while (false);
   
    return super.parseMsg(body, data);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll(" alias ", " @");
      Parser p = new Parser(field);
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, p.get(":"), data);
      data.strPlace = p.get();
      if (data.strPlace.startsWith("@")) data.strPlace = data.strPlace.substring(1).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = convertCodes(field, TYPE_CODES);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Properties CITY_TABLE = buildCodeTable(new String[]{
      "MAD",   "MADISON",
      "MDCO",  "MADISON COUNTY",
      "HSV",   "HUNTSVILLE",
      "LIME",  "LIMESTONE COUNTY",
      "LIME MAD", "MADISON",
      "NEWH",  "NEW HOPE",
      "OXRD",  "OWENS CROSS ROADS",
      "TRI",   "TRIANA"
  });
  
  private static final Properties TYPE_CODES = buildCodeTable(new String[]{
      "UNKMED",         "UNKNOWN MEDICAL",
      "M",              "MEDICAL",
      "FR",             "FIRE/RESCUE",
      "MVA/I",          "MVA W/ INJURY",
      "F/ALARMS",       "FIRE ALARM",
      "ALM/M",          "MEDICAL ALARM",
      "MVA/E",          "MVA W/ ENTRAPMENT",
      "F/OUTSIDE_FIRE", "OUTSIDE FIRE",
      "F/STRUCTURE",    "STRUCTURE FIRE"
  });
}
