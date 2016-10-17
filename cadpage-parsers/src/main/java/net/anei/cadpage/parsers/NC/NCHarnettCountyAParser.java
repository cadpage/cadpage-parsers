package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

/**
 * Harnett County, NC
 * 
 * This was replaced by NCHarnettCountyC after dispatch software vendor upgrade on 8/20/2014
 * retained only to process earlier calls in Cadpage history
 */
public class NCHarnettCountyAParser extends FieldProgramParser {
  
  private static final Pattern ID_PTN = Pattern.compile("\\d{4}-\\d{6}");
  
  public NCHarnettCountyAParser() {
    super(NCHarnettCountyParser.CITY_LIST, "HARNETT COUNTY", "NC",
           "ID? ADDR APT EMPTY ( EMPTY | CITY ( EMPTY X? | X/Z+? EMPTY ) ) EMPTY+? CALL! EMPTY EMPTY EMPTY UNIT");
  }
  
  @Override
  public String getFilter() {
    return "cadpage@harnett.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "911Communications:");
    body = stripFieldEnd(body, "*");
    
    if (ID_PTN.matcher(subject).matches()) data.strCallId = subject;
    
    String flds[] = body.split("\\*?\n");
    if (flds.length < 6) flds = body.split("\\*");
    else 
    if (flds.length < 6) return false;
    return parseFields(flds, data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private class MyIdField extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCallId.length() > 0) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!ID_PTN.matcher(field).matches()) abort();
      super.parse(field, data);
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) abort();
      String call = CALL_CODES.getCodeDescription(field);
      if (call != null) {
        data.strCode = field;
        field = call;
      }
      data.strCall = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final CodeTable CALL_CODES = new StandardCodeTable();
}
