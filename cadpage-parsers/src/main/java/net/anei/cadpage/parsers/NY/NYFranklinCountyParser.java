package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class NYFranklinCountyParser extends FieldProgramParser {

  public NYFranklinCountyParser() {
    super("FRANKLIN COUNTY", "NY", 
          "SRC CALL ADDR X? INFO+?");
  }
  
  @Override
  public String getFilter() {
    return "4702193648";
  }
  
  private static final Pattern MARKER = Pattern.compile("FRACO911:? +");
  private static final Pattern DELIM = Pattern.compile(" {2,}");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end());
    body = body.replace(" \n", " ");
    body = stripFieldEnd(body,  " CANCEL");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]+", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ")");
      Parser p = new Parser(field);
      String cross = p.getLastOptional('(');
      cross = stripFieldEnd(cross, "/");
      cross = stripFieldStart(cross, "/");
      data.strCross = cross;
      data.strCity = p.getLastOptional(',');
      if (ZIP_PTN.matcher(data.strCity).matches()) {
        String city = p.getLastOptional(',');
        if (city.length() > 0) data.strCity = city;
      }
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY X";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override 
    public boolean checkParse(String field, Data data) {
      if (data.strCross.length() == 0 && field.startsWith("(") && field.endsWith(")")) {
        parse(field, data);
        return true;
      }
      return false;
    }
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "(");
      field = stripFieldEnd(field, ")");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_TIME_PTN = Pattern.compile("\\d\\d:\\d\\d");
  private class MyInfoField extends InfoField {
    
    private boolean skip = false;
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strSupp.length() == 0) skip = false;
      if (skip) {
        skip = false;
        return true;
      }
      
      String time = getRelativeField(+1);
      if (!INFO_TIME_PTN.matcher(time).matches()) return false;
      field = append(time, " ", field);
      data.strSupp = append(data.strSupp, "\n", field);
      data.msgType = MsgType.RUN_REPORT;
      skip = true;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
