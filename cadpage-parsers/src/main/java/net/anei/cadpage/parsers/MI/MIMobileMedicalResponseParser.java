package net.anei.cadpage.parsers.MI;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

/**
 * Mobile Medical Response, MI
 * Covers Saginaw & Genesee & other counties
 */
public class MIMobileMedicalResponseParser extends DispatchProQAParser {
  
  private static final Pattern MISMATCH1_PTN = Pattern.compile("\\([^\\)]*$");
  private static final Pattern MISMATCH2_PTN = Pattern.compile("^[^\\(]*\\)");
  
  public MIMobileMedicalResponseParser() {
    super("", "MI", 
          "( ID! TIME? ADDR APT? | ADDR APT? INFO+? ID! ) CALL! XTRA+? INFO+? TIME");
  }
  
  @Override
  public String getFilter() {
    return "mailghost@mobilemedical.org,6344,6573";
  }
  
  @Override
  public String getLocName() {
    return "Mobile Medical Response, MI";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    // dummy loop statement
    do {
      if (subject.startsWith("<CAD>")) break;
      if (body.startsWith("--")) body = body.substring(2).trim();
      if (body.startsWith("<CAD>")) {
        body = body.substring(5).trim();
        break;
      }
      if (body.startsWith("/ <CAD> - part 1 of 1 / ")) {
        body = body.substring(24).trim();
        break;
      }
    } while (false);
    
    body = body.replace("\\b", "\n");
    body = stripFieldStart(body, "- part 1 of 1 --");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new AptField("\\d+[A-Z]?|[A-Z]", true);
    if (name.equals("XTRA")) return new ExtraField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('?', ' ');
      super.parse(field, data);
    }
  }

  // Field following call can be
  //   extension of call
  //   apt number
  //   or absent
  private class ExtraField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      
      // if call has an open left paren, and this field has an open right paren
      // Assume that this is an extension of the call description split by
      // an inadvertent / and put them back together again
      if (MISMATCH1_PTN.matcher(data.strCall).find() &&
          MISMATCH2_PTN.matcher(field).find()) {
        data.strCall = data.strCall + '/' + field;
        return true;
      }
      
      // If field length <= 3, assume it is an apt
      if (field.length() <= 3) {
        data.strApt = field;
        return true;
      }
      
      // Otherwise we can ignore this field and treat data as a normal info field
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      throw new RuntimeException("Not supported");
    }
    
    @Override
    public String getFieldNames() {
      return "APT";
    }
  }
  
  // Normal INFO field sometimes contain cross street info
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      if (field.equals("<Unknown>")) return;
      if (field.toUpperCase().startsWith("X ")) {
        data.strCross = append(data.strCross, " & ", field.substring(2).trim());
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "INFO X";
    }
  }
}
