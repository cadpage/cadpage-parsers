package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Montgomery County, TX
 */
public class TXMontgomeryCountyAParser extends FieldProgramParser {
  
  public TXMontgomeryCountyAParser() {
    super(CITY_CODES, "MONTGOMERY COUNTY", "TX",
           "New_Fire_Run:ID! Nature:CALL? UNIT! Location:ADDR/y! Building:PLACE Cross:X/c Grid:MAP Map:MAP");
  }
  
  @Override
  public String getFilter() {
    return "firecad@thewoodlandstownship-tx.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Sometime Nature field gets pulled out of text string and put in subject
    // When that happens we need to put it back
    if (subject.startsWith("Nature:")) {
      int pt = body.indexOf("New Fire Run:");
      if (pt < 0) return false;
      pt = body.indexOf(',', pt+13);
      if (pt < 0) return false;
      body = body.substring(0,pt+1) + subject + body.substring(pt+1);
    }
    if (parseFields(body.split(","), data)) return true;
    
    if (subject.equals("Fire CAD Message")) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }
    
    return false;
  }
  
  // Unit field, remove extraneous trailing semicolon
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(";")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("FUZZEL RD", "FUZZELL RD");
      super.parse(field, data);
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, "-", field);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CL",   "CLEVELAND",
      "CR",   "CONROE",
      "CS",   "CUT N SHOOT",
      "HC",   "HARRIS COUNTY",
      "HL",   "HOCKLEY",
      "HO",   "HOUSTON",
      "KW",   "KINGWOOD",
      "MA",   "MAGNOLIA",
      "MO",   "MONTGOMERY",
      "NC",   "NEW CANEY",
      "OR",   "OAK RIDGE NORTH",
      "PG",   "PATTON VILLAGE",
      "PV",   "PANORAMA VILLAGE",
      "PH",   "PINEHURST",
      "PO",   "PORTER",
      "RI",   "RICHARDS",
      "RF",   "ROMAN FOREST",
      "SC",   "SPRING",
      "SH",   "SHENANDOAH",
      "SP",   "SPLENDORA",
      "SG",   "SPRING",
      "ST",   "STAGECOACH",
      "TL",   "TIMBER LAKES",
      "WD",   "THE WOODLANDS",
      "WI",   "WILLIS",
      "WB",   "WOODBRANCH VILLAGE",
      "WH",   "WOODLOCH",

  });
}
