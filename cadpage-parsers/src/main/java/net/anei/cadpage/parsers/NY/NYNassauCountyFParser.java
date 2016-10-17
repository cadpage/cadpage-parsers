package net.anei.cadpage.parsers.NY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYNassauCountyFParser extends FieldProgramParser {
  
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");

  public NYNassauCountyFParser() {
    super("NASSAU COUNTY", "NY",
           "CALL1 CALL2 CALL3 CALL4 ADDR CITY PHONE!");
  }
  
  @Override
  public String getFilter() {
    return "2ndSignal@2sig.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (body.endsWith(" support@2sig.com")) {
      body = body.substring(0,body.length()-17).trim();
    }
    return parseFields(body.split("\n"), 6, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.startsWith("CALL")) {
      String ptn;
      switch (name.charAt(4)) {
      case '1':
        ptn = "[A-Z\\.]+";
        break;
      case '2':
        ptn = "[A-Z]{0,4}";
        break;
      default:
        ptn = "[-/ \\.A-Z0-9]*";
      }
      return new MyCallField(ptn);
    }
    if (name.equals("TIME")) return new TimeField(TIME_FMT);
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


