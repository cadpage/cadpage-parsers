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
          "DATETIME! Pri:PRI_CALL! PLACE! AD:ADDRCITY/S6! X! INFO/N+");
    setupProtectedNames("LEWIS AND CLARK");
  }
  
  @Override
  public String getFilter() {
    return "cad2@acuecc.org ";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD") && !subject.equals("CADPAGE")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  private static DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    if (name.equals("PRI_CALL")) return new MyPriorityCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X"))  return new MyCrossField();
    return super.getField(name);
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
      super.parse(field, data);
    }
  }
}
