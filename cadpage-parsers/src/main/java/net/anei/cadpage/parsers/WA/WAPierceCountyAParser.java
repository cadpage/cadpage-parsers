package net.anei.cadpage.parsers.WA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

/**
 * Pierce County, WA
 */
public class WAPierceCountyAParser extends DispatchProQAParser {
  
  private static final Pattern TRAILER_PTN = Pattern.compile(" +\\[\\d+\\]$");
  
  public WAPierceCountyAParser() {
    super("PIERCE COUNTY", "WA", 
           "PRI? ID! CALL CALL2? ( ADDR | PLACE ADDR | NAME NAME ADDR ) APT? EXTRA+");
  }
  
  @Override
  public String getFilter() {
    return "cadpage@rmetro.com,@usamobility.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    Matcher match = TRAILER_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start());
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new AptField("#(.*)|\\d{1,4}", true);
    if (name.equals("EXTRA")) return new MyExtraField();
    return super.getField(name);
  }
  
  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!CALL_EXT_SET.contains(field)) return false;
      super.parse(field, data);
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
      if (field.equals("<Unknown>")) return;
      super.parse(field, data);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      data.strName = append(data.strName, ", ", field);
    }
  }
  
  private static final Pattern ADDR_PTN = Pattern.compile(".*[\\d/&].*");
  private final class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!ADDR_PTN.matcher(field).matches() && checkAddress(field) == STATUS_NOTHING) return false;
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("\\d{3}");
  private static final Pattern MAP_PTN = Pattern.compile("\\d{3} [A-Z]\\d");
  private class MyExtraField extends Field {
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("X=")) {
        data.strCross = append(data.strCross, " & ", field.substring(2).trim());
      }
      
      else if (UNIT_PTN.matcher(field).matches()) {
        data.strUnit = append(data.strUnit, " ", field);
      }
      
      else if (data.strCity.length() == 0 && WAPierceCountyParser.CITY_SET.contains(field.toUpperCase())) {
        data.strCity = field;
      }
      
      else if (MAP_PTN.matcher(field).matches()) {
        data.strMap = field;
        return;
      }
      
      else {
        data.strSupp = append(data.strSupp, " / ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "X UNIT CITY MAP INFO";
    }
  }
  
  // List of call description extensions
  private static final Set<String> CALL_EXT_SET = new HashSet<String>(Arrays.asList(
      "Ingestion"
  ));
}
