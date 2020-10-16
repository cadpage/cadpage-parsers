package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;



public class SCYorkCountyParser extends FieldProgramParser {

  public SCYorkCountyParser() {
    super("YORK COUNTY", "SC",
          "( SELECT/3 ADDRCITY!" +
          "| SELECT/2 UNIT! LOC:ADDRCITY! NAR:INFO2! " + 
          "| UNIT! P:PRI! LOC:ADDRCITY! X:X! NAR:INFO " + 
          ") INC#:ID! END");
  }
  
  @Override
  public String getFilter() {
    return "paging@yorkcountygov.com";
  }
  
  private static final Pattern PREFIX_PTN = Pattern.compile("(?:((?:Initial|2nd|3rd|4th) Dispatch)|(Short Report)|(Call Complete))[- ]*");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = PREFIX_PTN.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end());
    } else {
      match = PREFIX_PTN.matcher(subject);
      if (!match.matches()) return false;
    }
    String selectValue = (match.group(1) != null ? "1" : match.group(2) != null ? "2" : "3");
    setSelectValue(selectValue);
    if (selectValue.equals("3")) data.msgType = MsgType.RUN_REPORT;
    
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }
  
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?"); 
  private class MyAddressCityField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher match =  ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        String zip = match.group(2);
        city = p.getLastOptional(',');
        if (city.length() == 0 && zip != null) city = zip;
      }
      data.strCity = city;
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY  ST";
    }
  }
  
  private static final Pattern CH_PREFIX_PTN = Pattern.compile("TAC|COMM", Pattern.CASE_INSENSITIVE);
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      
      Parser p = new Parser(field);
      data.strCross = p.get('*');
      data.strPlace = p.get('*');
      data.strCall = p.get('*');
      String channel = p.get('*');
      if (!channel.equals("None")) {
        if (CH_PREFIX_PTN.matcher(channel).lookingAt()) {
          int pt = channel.indexOf('/');
          if (pt >= 0) {
            data.strCall = append(data.strCall, " - ", stripFieldStart(channel.substring(pt+1).trim(), "/"));
            channel = channel.substring(0, pt).trim();
          }
        data.strChannel = channel;
        } else {
          data.strCall = append(data.strCall, " - ", channel);
        }
        data.strName = p.get();
  
        if (data.strPlace.length() > 0) {
          String tmp = new Parser(data.strPlace).get(' ');
          int ipt = data.strCity.indexOf(" " + tmp);
          if (ipt >= 0) data.strCity = data.strCity.substring(0,ipt).trim();
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "X PLACE CITY CALL CH NAME";
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("***");
      if (pt >= 0) {
        setGPSLoc(field.substring(pt+3).trim(), data);
        field = field.substring(0,pt).trim();
      }
      super.parse(field,data);
    }
    
    @Override
    public String getFieldNames() {
      return "ID GPS";
    }
  }
  
  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf("*");
      if (pt >= 0) {
        String channel = field.substring(pt+1).trim();
        if (CH_PREFIX_PTN.matcher(channel).lookingAt()) {
          data.strChannel = channel;
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CH";
    }
  }
}
