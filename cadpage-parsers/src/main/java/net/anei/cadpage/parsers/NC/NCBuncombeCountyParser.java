package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Buncombe county, NC
 */
public class NCBuncombeCountyParser extends DispatchOSSIParser {
  
  public NCBuncombeCountyParser() {
    super("BUNCOMBE COUNTY", "NC",
           "FYI? CHANGE? ASSREQ? ADDR! SRC? UNIT? SPEC+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@buncombecounty.org";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("S:") && body.endsWith(" M:")) {
      body = body.substring(2, body.length()-3).trim();
    }
    body = body.replaceAll("\n", " ");
    if (! super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) data.strCall = "Unknown";
    return true;
  }
  
  private static final Pattern CHANGED_PTN = Pattern.compile("Changed ([A-Za-z]+) from .*? to (.*)");
  private class MyChangeField extends CallField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CHANGED_PTN.matcher(field);
      if (!match.matches()) return false;
      String type = match.group(1);
      String newValue = match.group(2).trim();
      if (type.equals("Nature")) data.strCall = newValue;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
    }
  }
  
  /**
   * Call field that only trips if it contains the words "ASSISTANCE REQUESTED"
   */
  private class AssReqField extends CallField {
    
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      
      if (!field.contains("ASSISTANCE REQUESTED")) return false;
      parse(field, data);
      return true;
    }
  }
  
  /**
   * Covers fields entered in random order between the Unit and cross street
   */
  
  // Field will be treated as a call description if it contains
  // A something in parenthesis
  // A slash
  // Any of a set of words generally found in call descriptions
  private static final Pattern CALL_PATTERN = 
    Pattern.compile("^\\(.+\\)|/|\\b(?:FALL|FALLS|FIRE|PAIN|PAINS|HEART|PROBLEM|PROBLEMS|CHOKING|STROKE|SICK|INJURY|INJURIES|DAMAGE)\\b");
  private class SpecField extends Field {
    
    private Field callField = new CallField();
    private Field nameField = new NameField();
    private Field phoneField = new PhoneField();
    private Field idField = new IdField();
    private Field crossField = new CrossField();
    private Field infoField = new InfoField();

    @Override
    public void parse(String field, Data data) {
      
      String upField = field.toUpperCase();
      if (upField.startsWith("VERIZON")) return;
      if (upField.toUpperCase().startsWith("T-MOBILE")) return;
      if (upField.startsWith("SPRINT")) return;
      if (upField.startsWith("US CELLULAR")) return;
      if (upField.contains("DIST:")) return;
      
      Field fieldProc;
      if (field.contains(",")) {
        fieldProc = nameField;
      }
      
      else if (CALL_PATTERN.matcher(field).find()) {
        fieldProc = callField;
      }
      
      else if (field.length() == 10 && NUMERIC.matcher(field).matches()) {
        fieldProc = phoneField;
      }
      
      else if (field.length() == 8 && NUMERIC.matcher(field).matches()) {
        fieldProc = idField;
      }
      
      else if (!field.startsWith("FM") && checkAddress(field) == STATUS_STREET_NAME) {
        fieldProc = crossField;
      }
      
      else {
        fieldProc = infoField;
      }
      
      fieldProc.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL NAME PHONE INFO X ID";
    }
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("CHANGE")) return new MyChangeField();
    if (name.equals("ASSREQ")) return new AssReqField();
    if (name.equals("SRC")) return new SourceField("FS\\d+");
    if (name.equals("UNIT")) return new UnitField("[A-Z]{1,5}[0-9]{1,3}[A-Z]?(?:,.*)?", true);
    if (name.equals("SPEC")) return new SpecField();
    return super.getField(name);
  }
}
