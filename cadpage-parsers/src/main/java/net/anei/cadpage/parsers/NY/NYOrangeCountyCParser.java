package net.anei.cadpage.parsers.NY;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYOrangeCountyCParser extends FieldProgramParser {
  
  public NYOrangeCountyCParser() {
    super("ORANGE COUNTY", "NY",
        "SRC TYP:CALL! ADDR! CITY! Time:TIME? XST:X");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("mCAD")) return false;
    body = body.replace("TIME:", "|Time:");
    body = body.replace("TYP:", "|TYP:");
    body = body.replace("CMT1:", "/");
    body = body.replace("XST:", "|XST:");
    if (!parseFields(body.split("\\|"), data)) return false; 
    return true;
  }
    private class MyCityField extends CityField {
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('/');
      if (pt > 0) {
        data.strSupp = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CITY INFO";
    }
  }
    
  private static final Pattern INFO_PTN = Pattern.compile(" *\\b(?:INCIDENT CLONED FROM PARENT: F\\d+\\b|Parent Inc [A-Z0-9]+|UPDATE PriUnt to ([A-Z/0-9]+))\\b *");
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      StringBuffer sb = new StringBuffer(); 
      Matcher match = INFO_PTN.matcher(field); 
      while (match.find()) {
        String sUnit = match.group(1);
        if (sUnit != null) data.strUnit = sUnit;
        match.appendReplacement(sb, " ");
      }
      match.appendTail(sb);
      field = sb.toString().trim();

      super.parse(field, data);
    }
  }
   
    @Override
    protected Field getField(String name) {
      if (name.equals("INFO")) return new MyInfoField();
      if (name.equals("CITY")) return new MyCityField();
      return super.getField(name);
    }
}








