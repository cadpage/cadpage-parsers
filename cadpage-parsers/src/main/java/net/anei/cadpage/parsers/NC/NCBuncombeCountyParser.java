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
           "FYI? CHANGE? ASSREQ/SDS? PREFIX/SDS? CALL/SDS? ADDR! SRC? UNIT? SPEC+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@buncombecounty.org";
  }
  
  private boolean callFound;
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("S:") && body.endsWith(" M:")) {
      body = body.substring(2, body.length()-3).trim();
    }
    body = body.replaceAll("\n", " ");
    
    // Sanity check - must have at least one semicolons
    if (!body.contains(";")) return false;
    
    callFound = false;
    if (! super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) data.strCall = "Unknown";
    return true;
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("CHANGE")) return new MyChangeField();
    if (name.equals("ASSREQ")) return new AssReqField();
    if (name.equals("PREFIX")) return new CallField("UNDER CONTROL|Working fire", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("SRC")) return new SourceField("FS\\d+");
    if (name.equals("UNIT")) return new UnitField("[A-Z]{1,5}[0-9]{1,3}[A-Z]?(?:,.*)?", true);
    if (name.equals("SPEC")) return new SpecField();
    return super.getField(name);
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
  
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return false;
      if (Character.isDigit(field.charAt(0))) return false;
      if (!CALL_PATTERN.matcher(field).find()) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      callFound = true;
      super.parse(field, data);
    }
  }
  
  /**
   * Covers fields entered in random order between the Unit and cross street
   */
  
  // Field will be treated as a call description if it contains
  // A something in parenthesis
  // Any of a set of words generally found in call descriptions
  private static final Pattern CALL_PATTERN = 
    Pattern.compile("^\\(.+\\)|^\\{.+\\}|\\b(?:ASSAULT|ASSIST|CARDIAC|CHOKING|CONVULSIONS|DAMAGE|EMS|FALL|FALLS|FIRE|HEART|HEMORRHAGE|INJURY|INJURIES|PAIN|PAINS|PROBLEM|PROBLEMS|SEIZURES|SICK|SMOKE|STROKE|TRANSPORT|UNCONSCIOUS|WORKING)\\b", Pattern.CASE_INSENSITIVE);
  private class SpecField extends Field {
    
    private Field callField = new CallField();
    private Field nameField = new NameField();
    private Field phoneField = new PhoneField();
    private Field idField = new IdField();
    private Field crossField = new CrossField();
    private Field channelField = new ChannelField();
    private Field infoField = new InfoField();
    
    public SpecField() {
      callField.setQual("SDS");
    }

    @Override
    public void parse(String field, Data data) {
      
      field = stripFieldEnd(field, ",");
      
      String upField = field.toUpperCase();
      if (upField.startsWith("VERIZON")) return;
      if (upField.toUpperCase().startsWith("T-MOBILE")) return;
      if (upField.startsWith("SPRINT")) return;
      if (upField.startsWith("US CELLULAR")) return;
      if (upField.startsWith("AT&T MOBILITY")) return;
      if (upField.contains("DIST:")) return;
      
      String nextFld = getRelativeField(+1);
      if (nextFld.startsWith("Radio Channel:")) {
        nextFld = nextFld.substring(14).trim();
        if (field.equals(nextFld)) return;
      }
      
      Field fieldProc;
      if (field.startsWith("Radio Channel:")) {
        field = field.substring(14).trim();
        fieldProc = channelField;
      }
      
      else if (data.strName.length() == 0 && field.length() <= 35 && field.contains(",")) {
        fieldProc = nameField;
      }
      
      else if (data.strPhone.length() == 0 && field.length() == 10 && NUMERIC.matcher(field).matches()) {
        fieldProc = phoneField;
      }
      
      else if (data.strCallId.length() == 0 && field.length() == 8 && NUMERIC.matcher(field).matches()) {
        fieldProc = idField;
      }
      
      else if (!field.startsWith("FM") && checkAddress(field) == STATUS_STREET_NAME) {
        fieldProc = crossField;
      }
      
      else if (!callFound && CALL_PATTERN.matcher(field).find()) {
        callFound = true;
        fieldProc = callField;
      }
      
      else {
        fieldProc = infoField;
      }
      
      fieldProc.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL NAME PHONE INFO X ID CH";
    }
  }
}
