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
          "LOC:ADDR! AD:PLACE? DESC:PLACE? APT:APT? CRSTR:X! TYP:CODE CMT:INFO! CASE__#:ID? USER_ID:SKIP? CREATED:SKIP? INC:ID TIME:SKIP");
    this.callCodes = callCodes;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    if (!super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) {
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
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CODE", "CODE CALL");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }
  
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "*M*");
      field = stripFieldEnd(field, "-");
      super.parse(field, data);
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
