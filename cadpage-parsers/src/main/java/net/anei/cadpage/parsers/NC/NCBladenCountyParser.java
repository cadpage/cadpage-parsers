package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCBladenCountyParser extends FieldProgramParser {
  
  public NCBladenCountyParser() {
    super("BLADEN COUNTY", "NC", 
          "ID CALL ADDR DUPADDR? DUPCITY? UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "PageGate@bladenco.org";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("As of (\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d:\\d\\d:\\d\\d)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    
    return parseFields(body.split("\n"), 4, data);
  }
  
  @Override
  public String getProgram() {
    return "DATE TIME " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DUPADDR")) return new MyDupAddressField();
    if (name.equals("DUPCITY")) return new MyDupCityField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt =  field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private class MyDupAddressField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals(getRelativeField(-1));
    }
  }
  
  private class MyDupCityField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals(data.strCity);
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Towns
    "BLADENBORO",
    "CLARKTON",
    "DUBLIN",
    "EAST ARCADIA",
    "ELIZABETHTOWN",
    "TAR HEEL",
    "WHITE LAKE",

    // Census-designated places
    "BUTTERS",
    "KELLY",
    "WHITE OAK",

    // Unincorporated communities
    "ABBOTTSBURG",
    "AMMON",
    "AMMON FORD",
    "COLLY TOWNSHIP",
    "COUNCIL",
    "ROSINDALE",

    // Townships
    "ABBOTTSBURG",
    "BETHEL",
    "BLADENBORO",
    "BROWN MARSH",
    "CARVERS CREEK",
    "CENTRAL",
    "CLARKTON",
    "COLLY",
    "CYPRESS CREEK",
    "ELIZABETHTOWN",
    "EAST ARCADIA",
    "FRENCHES CREEK",
    "HOLLOW",
    "LAKE CREEK",
    "TARHEEL",
    "TURNBULL",
    "WHITE OAK",
    "DUBLIN",
    "WHITES CREEK"
  };
}
