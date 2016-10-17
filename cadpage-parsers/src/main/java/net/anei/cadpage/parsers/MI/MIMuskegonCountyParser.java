package net.anei.cadpage.parsers.MI;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class MIMuskegonCountyParser extends DispatchOSSIParser {
  
  // Too frigging many optional fields.
  // If there is no FYI field there is always a CALL field and may be a place field
  // If there is an FYI field, then CALL and PLACE are both optional, we have to count on the
  // CALL meeting a very limited set of value criteria
  // To make matters worse, the address is followed by a cross street that will be identified as
  // an address, so we have to use a lot of complicated conditional branches to check the first
  // field that looks like an address
  public MIMuskegonCountyParser() {
    super(CITY_CODES, "MUSKEGON COUNTY", "MI",
           "( FYI CALL? | CALL ) ( ADDR! | PLACE ADDR! | ADDR ) X? X? CITY? ( PLACE CALL | CALL2? ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "cad@mcd911.net,9300";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_CR;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    if (! super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) data.strCall = "Unknown";
    return true;
  }
  
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.endsWith("- WORKING FIRE") && !CALL_SET.contains(field)) return false;
      parse(field, data);
      return true;
    }
  }
  
  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCall.length() > 0) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  private static final Pattern CITY_PTN = Pattern.compile("[A-Z]{4}");
  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!CITY_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("STRUCTFIRE")) return new CallField("STRUCTURE FIRE");
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BLTW", "BLUE LAKE TWP",
      "CCTW", "CEDAR CREEK TWP",
      "CSVL", "CASNOVIA VILLAGE",
      "CSTW", "CASNOVIA TWP",
      "EGTW", "EGELSTON TWP",
      "FLTW", "FRUITLAND TWP",
      "FPTW", "FRUITPORT TWP",
      "FPVL", "FRUITPORT",
      "HLTW", "HOLTON TWP",
      "LTTW", "LAKETON TWP",
      "MCTW", "MUSKEGON CHARTER TWP",
      "MHCT", "MUSKEGON HEIGHTS",
      "MNCT", "MONTAGUE",
      "MNTW", "MONTAGUE TWP",
      "NMCT", "NORTH MUSKEGON",
      "NSCT", "NORTON SHORES",
      "RPCT", "ROOSEVELT PARK",
      "RVTW", "RAVENNA TWP",
      "RVVL", "RAVENNA VILLAGE",
      "SUTW", "SULLIVAN TWP",
      "MRTW", "MOORLAND TWP",
      "MUCT", "MUSKEGON",
      "WHCT", "WHITEHALL",
      "WHTW", "WHITEHALL TWP",
      "LWCL", "LAKEWOOD CLUB",
      "WRTW", "WHITE RIVER TWP"
  });
  
  private static final Set<String> CALL_SET = new HashSet<String>(Arrays.asList(new String[]{
      "COMMERCIAL FIRE",
      "COMMERICAL FIRE",  // Dispatch spelling??
      "ICE RESCUE",
      "MEDICAL EMERGENCY-PRIORITY 1",
      "STRUCTURE FIRE"
  }));
}
