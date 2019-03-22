package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA52Parser extends FieldProgramParser {
  
  private Properties callCodes;

  public DispatchA52Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA52Parser(Properties callCodes, String defCity, String defState) {
    this(callCodes, null, defCity, defState);
  }

  public DispatchA52Parser(Properties callCodes, Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState, 
          "( TYPN:CALL! MODCIR:CODE? AD:PLACE! LOC:ADDR! APT:APT? CRSTR:X? UNS:UNIT! TIME:DATETIME3% INC:ID% ZIP:ZIP% GRIDREF:MAP% " + 
          "| LOC:ADDR! AD:PLACE? DESC:PLACE? BLD:APT? FLR:APT? APT:APT? CRSTR:X TYP:CODE1 MODCIR:CODE2 " + 
                "( TIME:DATETIME3! UNS:UNIT! TYPN:CALL! INC:ID!" +
                "| CMT:INFO! CC:SKIP? CC_TEXT:CALL CC:INFO/N? CASE__#:ID? USER_ID:SKIP? CREATED:SKIP? INC:ID UNS:UNIT TYPN:SKIP TIME:SKIP " +
                ") " +
          ") END");
          
    this.callCodes = callCodes;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    if (body.startsWith("LOC:APPROX LOC:")) body = body.substring(11);
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CODE", "CODE CALL?");
  }
  
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEEEEE, MMMM dd, yyyy hh:mm:ss aa");
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE1")) return new MyCode1Field();
    if (name.equals("CODE2")) return new MyCode2Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("DATETIME3")) return new DateTimeField(DATE_TIME_FMT);
    if (name.equals("ZIP")) return new MyZipField();
    return super.getField(name);
  }
  
  private class MyCode1Field extends CodeField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "*M*");
      field = stripFieldEnd(field, "-");
      super.parse(field, data);
      if (callCodes == null) {
        data.strCall = data.strCode;
        data.strCode = "";
      } else {
        String call = callCodes.getProperty(data.strCode);
        if (call != null) {
          data.strCall = call;
        } else {
          data.strCall = data.strCode;
        }
      }
    }
  }
  
  private class MyCode2Field extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      
      if (data.strCode.length() == 0) {
        data.strCall = append(data.strCall, " ", field);
      }
      else if (data.strCall.equals(data.strCode)) {
        data.strCode = data.strCall = append(data.strCode, " ", field);
      } else {
        data.strCode = append(data.strCode, " ", field);
      }
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (data.strCode.length() == 0) {
        data.strCode = data.strCall;
        data.strCall = field;
      } else if (data.strCode.equals(data.strCall)) {
        data.strCall = field;
      } else {
        data.strCall = append(data.strCall, " - ", field);
      }
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("/")) field = field.substring(0,field.length()-1).trim();
      if (field.startsWith("/")) field = field.substring(1).trim();
      super.parse(field, data);
    }
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCallId.length() > 0) return;
      super.parse(field, data);
    }
  }
  
  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }
}
