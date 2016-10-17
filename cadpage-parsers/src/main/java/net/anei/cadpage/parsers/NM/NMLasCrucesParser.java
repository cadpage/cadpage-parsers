package net.anei.cadpage.parsers.NM;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NMLasCrucesParser extends FieldProgramParser {
  public NMLasCrucesParser() {
    super("LAS CRUCES", "NM", 
        "CALL! LOCATION! EMPTY ID! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "access@mvrda.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int inParen = 0;
    for (int i=0; i<body.length(); i++) {
      if (body.charAt(i) == ')')
        inParen--;
      else if (body.charAt(i) == '(')
        inParen++;
      else if (inParen == 0 && body.charAt(i) == ';')
        body = body.substring(0, i)+"\\;"+body.substring(++i);
    }
    if (inParen != 0) return false;
    String[] field = body.split("\\\\;");
    return parseFields(field, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("LOCATION")) return new LocationField();
    if (name.equals("ID")) return new IdField("AMR\\d{4}-\\d{5}", true);
    return super.getField(name);
  }
 
  private static final Pattern PAREN_PATTERN = Pattern.compile("(.*?)\\((.*)\\)(.*)");
  private static final Pattern PLACE_PATTERN = Pattern.compile("Near:(.*)");
  private static final Pattern ADDRESS_PATTERN = Pattern.compile("(.*?),\\s*(?:(\\d{5})|0)(?: *#(.*))?");
  private class LocationField extends Field {
    @Override
    public void parse(String field, Data data) {
      String parenthesized;
      Matcher m = PAREN_PATTERN.matcher(field);
      if (m.matches()) {
        field = append(m.group(1).trim(), " ", m.group(3).trim());
        parenthesized = m.group(2);
      }
      else parenthesized = "";
      
      parseField(field, data);
      if (!parenthesized.equals("")) parsePFields(parenthesized, data);
    }
    
    private void parseField(String field, Data data) {
      if (field.charAt(0) == '@') {
        data.strPlace = field.substring(1).trim();
      } else {
        parseStreet(field, data);
      }
    }
    
    private void parseStreet(String field, Data data) {
      Matcher m = ADDRESS_PATTERN.matcher(field);
      if (m.matches()) {
        parseAddress(m.group(1), data);
        data.strCity = getOptGroup(m.group(2));
        String apt = getOptGroup(m.group(3));
        if (apt != null && !apt.equals(data.strApt)) {
          data.strApt = append(data.strApt, "-", apt);
        }
      }
      else
        parseAddress(field, data);
    }
    
    private void parsePFields(String pField, Data data) {
      String[] pf = pField.split(";");
      for (int i=0; i< pf.length; i++) {
        parsePFieldPiece(pf[i].trim(), data);
      }
    }
    
    private void parsePFieldPiece(String pfp, Data data) {
      if (pfp.equals(""))
        return;
      if (data.strAddress.equals("")) {
        parseAddress(pfp, data);
        return;
      }
      Matcher m = PLACE_PATTERN.matcher(pfp);
      if (m.matches()) {
        data.strPlace = m.group(1).trim();
        return;
      }
      if (pfp.contains("/")) {
        data.strCross = append(data.strCross, "/", pfp);
        return;
      }
      
      // Can't do anything else with it - put in INFO
      data.strSupp = append(data.strSupp, " ", pfp);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR X APT PLACE CITY INFO";
    }
  }
}
