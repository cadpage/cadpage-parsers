package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA50Parser extends FieldProgramParser {
  
  private Properties callCodes;
  
  public DispatchA50Parser(Properties cityCodes, String defCity, String defState) {
    this(null, cityCodes, defCity, defState);
  }
  
  public DispatchA50Parser(Properties callCodes, Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "Location:ADDR/2S? EID:ID! TYPE_CODE:CALL! CALLER_NAME:NAME CALLER_ADDR:ADDR/S TIME:TIME Comments:INFO  SPECIAL_ADDRESS_COMMENT:INFO/N Disp:UNIT");
    this.callCodes =  callCodes;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern PLACE_MARKER = Pattern.compile(" *:? *@ *| *: *alias *| *\\*+ *");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.length() > 0) return;
      
      String sPlace = "";
      Matcher match = PLACE_MARKER.matcher(field);
      if (match.find()) {
        sPlace = field.substring(match.end()).trim();
        field = field.substring(0,match.start()).trim();
      }
      String apt = "";
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      field = stripFieldEnd(field, ": EST");
      super.parse(field, data);
      data.strAddress = stripFieldEnd(data.strAddress, "-");
      data.strApt = append(data.strApt, "-", apt);

      Result res = parseAddress(StartType.START_OTHER, FLAG_IGNORE_AT | FLAG_ONLY_CITY | FLAG_ANCHOR_END, sPlace);
      if (res.isValid()) {
        res.getData(data);
        sPlace = res.getStart();
      }
      data.strPlace = sPlace;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() >= 2 && field.charAt(1) == '-') {
        data.strPriority = field.substring(0,1).trim();
        field = field.substring(2).trim();
      }
      if (callCodes != null) field = convertCodes(field, callCodes);
      data.strCall = field;
    }
    
    @Override
    public String getFieldNames() {
      return "PRI CODE CALL";
    }
  }
  
  private static final Pattern INFO_TRIM_PTN = Pattern.compile("[- *=_]{3,}$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_TRIM_PTN.matcher(field);
      if (match.find()) field = field.substring(0,match.start());
      super.parse(field, data);
    }
  }
}
