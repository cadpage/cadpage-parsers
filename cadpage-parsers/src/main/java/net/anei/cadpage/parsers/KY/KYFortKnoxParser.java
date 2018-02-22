package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class KYFortKnoxParser extends FieldProgramParser {

  public KYFortKnoxParser() {
    super("FORT KNOX", "KY", 
          "INCIDENT:ID! TITLE:CALL! PLACE:PLACE? ADDRESS:ADDR? CITY:CITY? STATE:ST? GPS:GPS! ( BOX:BOX! | Box:BOX! | ) NOTES:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "monacoenterprises2014@gmail.com,fkfd469@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("[CLOSED]")) {
        data.msgType = MsgType.RUN_REPORT;
      }
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile(" *(?:Incident created|Incident closed|\\S+ changed)$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_JUNK_PTN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
  }
}
