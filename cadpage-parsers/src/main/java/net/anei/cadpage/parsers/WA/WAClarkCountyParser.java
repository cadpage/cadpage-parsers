package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class WAClarkCountyParser extends FieldProgramParser {
  
  private static final Pattern GEN_ALERT_PTN = Pattern.compile("([^ ]*) *\\bMPU: *(.*)");
  
  private String select;
  
  public WAClarkCountyParser() {
    super(CITY_CODES, "CLARK COUNTY", "WA",
           "SRC LOC:ADDR/S! ( MAP:MAP! OPS:CALL! | ) SUB_TYPE:CODE! PRI:PRI! TIME:TIME! EV#:ID! ALARM:SKIP! Disp:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "777,888,CRESA CAD";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = GEN_ALERT_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = "GENERAL ALERT";
      data.strSource = match.group(1);
      data.strPlace = match.group(2);
      return true;
    }
    
    body = body.replace(" UNITS:", " Disp:");
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("Clark County")) data.strCity = "";
    return true;
  }
  
  @Override
  public String getSelectValue() {
    return select;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // Next field will generally be a MAP: field.  If it is not, then this
      // is an OOC mutual aid call.  THere is not MAP or OPS: field, the 
      // call description is concatenated with the address, hopefully with a
      // double blank separator, and there is no city
      if (!getRelativeField(+1).startsWith("MAP:")) {
        data.defCity = "";
        int pt = field.lastIndexOf("  ");
        if (pt >= 0) {
          data.strCall = field.substring(pt+2).trim();
          super.parse(field.substring(0,pt).trim(), data);
        } else {
          parseAddress(StartType.START_ADDR, field, data);
          data.strCall = getLeft();
          if (data.strCall.length() == 0) abort();
        }
        
      } else {
        Parser p = new Parser(field);
        data.strPlace = p.getLastOptional(": @");
        data.strApt = p.getLastOptional(',');
        super.parse(p.get(), data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE CALL";
    }
  }
  
  private static final Pattern OPS_CALL_PTN = Pattern.compile("(OPS\\d+) +(.*)"); 
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = OPS_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CH CALL";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BG",   "Battleground",
      "CAM",  "Camas",
      "CLK",  "Clark County",
      "CPD",  "Camas",
      "LCPD", "La Center",
      "RPD",  "Ridgefield",
      "VAN",  "Vancouver",
      "VPD",  "Vancouver",
      "WAS",  "Washougal",
      "WPD",  "Washougal"
      
  });
}
