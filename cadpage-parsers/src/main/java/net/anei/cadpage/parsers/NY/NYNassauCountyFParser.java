package net.anei.cadpage.parsers.NY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYNassauCountyFParser extends FieldProgramParser {

  public NYNassauCountyFParser() {
    super("NASSAU COUNTY", "NY",
           "CALL1 ( PHONE! | CALL2 CALL3 CALL4 ADDR CITY PHONE! )");
  }
  
  @Override
  public String getFilter() {
    return "2ndSignal@2sig.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    body = stripFieldEnd(body, " support@2sig.com");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.startsWith("CALL")) {
      String ptn;
      switch (name.charAt(4)) {
      case '1':
        ptn = "[A-Z\\.]+\\d*";
        break;
      case '2':
        ptn = "[A-Z]{0,4}";
        break;
      default:
        ptn = "[-/ \\.A-Z0-9]*";
      }
      return new MyCallField(ptn);
    }
    if (name.equals("PHONE")) return new PhoneField("(?:1-)?\\d{3}-\\d{3}-\\d{4}");
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    
    public MyCallField(String pattern) {
      super(pattern, true);
    }
    
    @Override 
    public void parse(String field, Data data) {
      // Odd duplicated call fields
      int len = field.length();
      if ((len % 2) == 0) {
        len /= 2;
        String tmp = field.substring(0,len);
        if (tmp.equals(field.substring(len))) field = tmp;
      }
      data.strCall = append(data.strCall, " - ", field);
    }
  }
}


