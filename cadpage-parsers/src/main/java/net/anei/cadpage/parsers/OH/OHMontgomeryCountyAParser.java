package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class OHMontgomeryCountyAParser extends FieldProgramParser {
  
  public OHMontgomeryCountyAParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "OH",
           "CALL ADDR/S MAP UNIT DATETIME CFS_#:ID!");
  }
  
  @Override
  public String getFilter() {
    return "CADsystem@hhoh.org";
  }
  
  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    
    if(!subject.startsWith("CAD Page")) return false;

    body = body.replaceAll("\nCFS# ", "\nCFS #: ");
    String[] fields = body.split("\n");
    return parseFields(fields, data);
  }
  
  private static final Pattern ADDR_ST_PTN = Pattern.compile("(.*), *OH");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      
      Matcher match = ADDR_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
      }
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class MyMapField extends MapField {
    
    @Override 
    public void parse(String field, Data data) {
      if(field.endsWith("/")) {
        field = field.substring(0, field.length()-1).trim();   // Remove slash if nothing comes after it.
      }
      
      super.parse(field, data);
    }
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  @Override 
  public String adjustMapAddress(String sAddress) {
    
    // Replace @ with & and spaces to make addressing easier
    sAddress = sAddress.replace("@", " & ");
    
    // I have not found an "Interstate" that will map but "OH" followed by the number usually maps
    sAddress = sAddress.replace("INTERSTATE", "OH");
    
    return sAddress;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "BROOKVILLE",
    "CARLISLE",
    "CENTERVILLE",
    "CLAYTON",
    "DAYTON",
    "ENGLEWOOD",
    "GERMANTOWN",
    "HUBER HEIGHTS",
    "KETTERING",
    "MIAMISBURG",
    "MORAINE",
    "OAKWOOD",
    "RIVERSIDE",
    "SPRINGBORO",
    "TROTWOOD",
    "UNION",
    "VANDALIA",
    "WEST CARROLLTON",
    
    // Villages
    "FARMERSVILLE",
    "NEW LEBANON",
    "PHILLIPSBURG",
    "VERONA",
    
    // Townships
    "BUTLER TOWNSHIP",
    "CLAY TOWNSHIP",
    "GERMAN TOWNSHIP",
    "HARRISON TOWNSHIP",
    "JACKSON TOWNSHIP",
    "JEFFERSON TOWNSHIP",
    "MIAMI TOWNSHIP",
    "PERRY TOWNSHIP",
    "WASHINGTON TOWNSHIP",

  };
}
