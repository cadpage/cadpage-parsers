package net.anei.cadpage.parsers.VA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Albemarle County, VA (C)
 */

public class VAAlbemarleCountyCParser extends FieldProgramParser {
  
  public VAAlbemarleCountyCParser() {
    super("ALBEMARLE COUNTY", "VA",
          "( DATETIME! Pri:PRI_CALL! PLACE! AD:ADDRCITY/S6! X! INFO/N+? UNIT! END | CALL ADDRCITY X/Z? PLACE UNIT! INFO/N+ )");
    setupProtectedNames("LEWIS AND CLARK");
  }
  
  @Override
  public String getFilter() {
    return "cad2@acuecc.org,jplumb@albemarle.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD") && !subject.equals("CADPAGE")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("PRI_CALL")) return new MyPriorityCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X"))  return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z]+\\d{1,3}(?:A|ALS|CHF|DUTY)?|COUNTY(?:BC\\d+|DUTY|FM)?|VOL)\\b[, ]*)+", true);
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)"); 
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate =  match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern PRIORITY_CALL_PTN = Pattern.compile("([A-Z0-9]+) +(.*)"); 
  private class MyPriorityCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PRIORITY_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPriority = match.group(1);
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "PRI CALL";
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override 
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Searching Cross Streets...")) return;
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(",")) return;
      super.parse(field, data);
    }
  }
}
