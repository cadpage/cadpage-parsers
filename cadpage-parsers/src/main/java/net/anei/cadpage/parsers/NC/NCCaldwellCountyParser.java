package net.anei.cadpage.parsers.NC;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCCaldwellCountyParser extends FieldProgramParser {
  
  public NCCaldwellCountyParser() {
    super(CITY_CODES, "CALDWELL COUNTY", "NC",
           "CALL ( ADDR/Z ID | ADDR/Z CITY APT? X/Z+? ID | PLACE ADDR/Z ID | PLACE ADDR/Z CITY APT? X/Z+? ID )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@caldwellcountync.org,7677";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  private class MyAptField extends AptField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("APT ")) return false;
      super.parse(field.substring(4).trim(), data);
      return true;
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    return super.getField(name);
  }
  
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "GF",   "GRANITE FALLS",
      "GMW",  "GAMEWELL",
      "GC",   "GRACE CHAPEL",
      "HICK", "HICKORY",
      "HUD",  "HUDSON",
      "KC",   "KINGS CREEK",
      "LEN",  "LENOIR",
      "LR",   "LITTLE RIVER",
      "NC",   "NORTH CATAWBA",
      "PATT", "PATTERSON",
      "RHOD", "RHODHISS",
      "SAW",  "SAWMILLS",
      "VAL",  "VALMEAD",
      "YAD",  "YADKIN VALLEY",
      "BETH", "BETHLEHEM"
  });
  
}
