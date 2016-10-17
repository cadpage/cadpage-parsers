package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MIOaklandCountyCParser extends FieldProgramParser {

  public MIOaklandCountyCParser() {
    this("OAKLAND COUNTY", "MI");
  }

  MIOaklandCountyCParser(String defCity, String defState) {
    super(defCity, defState, 
          "CALL ADDR ADDR2? APT? PLACE+? PHONE END");
  }
  
  @Override
  public String getAliasCode() {
    return "MIOaklandCounty";
  }
  
  @Override
  public String getFilter() {
    return "@oakgov.com";
  }
  
  private static final String SIGNATURE = "https://apps.clemis.org/";
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith(SIGNATURE)) return false;
    if (!body.startsWith(SIGNATURE)) return false;
    int pt = body.indexOf(" /");
    if (pt < 0) return false;
    body = body.substring(pt+2).trim();
    return parseFields(body.split("/"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("PHONE")) return new PhoneField("CALLBK= *(.*)", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_STREET_NO_PTN = Pattern.compile("[-0-9]+ +.*");
  private class MyAddress2Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override 
    public boolean checkParse(String field, Data data) {
      
      // Treat as send part of intersection if the first address does not
      // appear to have street number.  The checkAddress call is there to
      // prevent 124 ST from being treated as a complete address
      if (!ADDR_STREET_NO_PTN.matcher(field).matches() && 
          checkAddress(data.strAddress) != STATUS_STREET_NAME) return false;
      data.strAddress = append(data.strAddress, " & ", field);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|SUITE|LOT|RM|ROOM) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (!match.matches()) return false;
      String apt = match.group(1);
      if (!apt.equals(data.strApt)) data.strApt = append(data.strApt,"-", apt);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      
      // Could be a place name or could be a cross street
      if (field.startsWith("X OF ")) {
        data.strCross = append(data.strCross, " / ", field.substring(5).trim());
        return;
      }
      
      if (isValidAddress(field)) {
        data.strCross = append(data.strCross," / ", field);
        return;
      }
      
      data.strPlace = append(data.strPlace, "/", field);
    }
    
    @Override
    public String getFieldNames() {
      return "X PLACE";
    }
  }
}
