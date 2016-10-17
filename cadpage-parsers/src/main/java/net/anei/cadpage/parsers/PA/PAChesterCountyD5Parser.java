package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;




public class PAChesterCountyD5Parser extends PAChesterCountyBaseParser {
  
  private static final Pattern DELIM = Pattern.compile("\\*\\*");
  
  public PAChesterCountyD5Parser() {
    super("AGENCY TIME CALL ADDRCITY APTPL X BOX! CITY INFO+");
  }
  
  @Override
  public String getFilter() {
    return "adi62@ridgefirecompany.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.length() == 0) return false;
    data.strSource = subject;
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();

    // Split and parse by double asterisk delimiters
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  // Call field strips trailing asterisk marker
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(" *")) field = field.substring(0,field.length()-2);
      super.parse(field, data);
    }
  }
  
  // Apt - place field
  private static final Pattern APT_PTN = Pattern.compile("(?:RM|#|ROOM|SUITE) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("-")) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = match.group(1);
      } else {
        data.strPlace = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("AGENCY")) return new SkipField("[A-Z]{4}", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("APTPL")) return new MyAptPlaceField();
    if (name.equals("BOX")) return new BoxField("\\d{4}", true);
    return super.getField(name);
  }
} 
