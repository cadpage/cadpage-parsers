package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCAlexanderCountyParser extends DispatchOSSIParser {
  
  public NCAlexanderCountyParser() {
    super(CITY_CODES, "ALEXANDER COUNTY", "NC",
          "FYI? ( CALL2 ADDR CITY INFO+ " +
               "| SRC? ADDR ( CALL! ( END | CODE | X ) | PLACE CALL! CODE? ) X+ )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@alexandercountync.gov,6504224256";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (body.length() == 0) body = subject;
    body = stripFieldStart(body, "|");
    if (subject.length() == 0 && body.startsWith("Text Message / ")) {
      subject = "Text Message";
      body = body.substring(15).trim();
    }
    if (!body.startsWith("CAD:") && 
        (subject.equals("Text Message") || isPositiveId())) {
      body = "CAD:" + body;
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("SRC")) return new SourceField("[A-Z]+", true);
    if (name.equals("CODE")) return new CodeField("\\d{1,2}[A-Z]\\d{1,2}[A-Za-z]?", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final Pattern CALL_CH_PTN = Pattern.compile("(.+?) CHANNEL # (\\d+)");
  private class MyCall2Field extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      // See if this is one of our special call fields
      if (!field.startsWith("CANCEL") && !field.equals("UNDER CONTROL")) {
        if (!field.startsWith("{")) return false;
        int pt = field.indexOf('}');
        if (pt < 0) return false;
        data.strSource = field.substring(1,pt).trim();
        field = field.substring(pt+1).trim();
      }
      Matcher match = CALL_CH_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = match.group(1) + ' ' +  match.group(2);
      } else {
        data.strCall = field;
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "SRC CALL CH";
    }
  }
  
  private static final Pattern CROSS_MARK_PTN = Pattern.compile("\\b(?:NC|US)\\b");
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (CROSS_MARK_PTN.matcher(field).find()) {
        super.parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "GF",  "GRANITE FALLS",
      "HID", "HIDDENITE",
      "HKA", "HICKORY",
      "MOR", "MORAVIAN FALLS",
      "STP", "STONY POINT",
      "SVA", "STATESVILLE",
      "TAY", "TAYLORSVILLE TWP"
  });
  
}
