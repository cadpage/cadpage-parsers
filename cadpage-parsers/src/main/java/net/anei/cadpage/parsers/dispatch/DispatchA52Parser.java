package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA52Parser extends FieldProgramParser {
  
  private Properties callCodes;

  public DispatchA52Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA52Parser(Properties callCodes, String defCity, String defState) {
    super(defCity, defState, 
          "LOC:ADDR! AD:PLACE? DESC:PLACE? BLD:APT? FLR:APT? APT:APT? CRSTR:X TYP:CODE1 MODCIR:CODE2 CMT:INFO! CC:SKIP? CC_TEXT:CALL CC:INFO/N? CASE__#:ID? USER_ID:SKIP? CREATED:SKIP? INC:ID UNS:UNIT TYPN:SKIP TIME:SKIP");
    this.callCodes = callCodes;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CODE", "CODE CALL");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE1")) return new MyCode1Field();
    if (name.equals("CODE2")) return new MyCode2Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
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
}
