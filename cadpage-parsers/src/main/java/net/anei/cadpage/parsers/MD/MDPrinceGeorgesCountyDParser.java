package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Prince Georges County, MD
 */
public class MDPrinceGeorgesCountyDParser extends MDPrinceGeorgesCountyBaseParser {
  
  private static Pattern MARKER = Pattern.compile("^\\d+: ");
  
  public MDPrinceGeorgesCountyDParser() {
    super("CALL! At:DATETIME! Dispatch:SKIP? Backup:SKIP? All:UNIT! To:ADDR!");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    data.strSource = new Parser(subject).getLast('|');;
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.end()).replace('\n', ' ').trim();
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt >= 0) {
          data.strCross = field.substring(pt+1, field.length()-1).trim();
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X";
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
}
