package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class INOwenCountyParser extends FieldProgramParser {
  
  public INOwenCountyParser() {
    super("OWEN COUNTY", "IN", 
          "Incident:CALL! Address:ADDR! Units:UNIT! Response_Times:TIMES!");
  }
  
  @Override
  public String getFilter() {
    return "zuercher@owencounty.in.gov";
  }
  
  private static final String MARKER = " Please respond immediately.";
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (body.startsWith("Incident:")) {
      if (!subject.equals("None")) data.strCallId = subject;
      return super.parseMsg(body, data);
    }
    
    setFieldList("CALL ADDR APT CITY ST INFO");
    data.strCall = subject;
    int pt = body.indexOf(MARKER);
    if (pt < 0) return false;
    parseMyAddress(body.substring(0, pt).trim(), data);
    data.strSupp = body.substring(pt+MARKER.length()).trim();
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID? " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR"))  return new MyAddressField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      parseMyAddress(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
  
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private static final Pattern ST_PTN = Pattern.compile("[A-Z]{2}");

  private void parseMyAddress(String addr, Data data) {
    String zip = null;
    Matcher match = ADDR_ZIP_PTN.matcher(addr);
    if (match.matches()) {
      addr = match.group(1).trim();
      zip = match.group(2);
    }
    
    Parser p = new Parser(addr);
    String city = p.getLastOptional(',');
    if (ST_PTN.matcher(city).matches()) {
      data.strState = city;
      city = p.getLastOptional(',');
    }
    data.strCity = city;
    parseAddress(p.get(), data);
    
    if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
  }
  
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() > 0) data.msgType = MsgType.RUN_REPORT;
      super.parse(field, data);
    }
  }
  
}
