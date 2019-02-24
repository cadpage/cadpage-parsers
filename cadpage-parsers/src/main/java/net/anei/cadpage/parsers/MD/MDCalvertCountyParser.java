package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;



public class MDCalvertCountyParser extends FieldProgramParser {
  
  public MDCalvertCountyParser() {
    super("CALVERT COUNTY", "MD", 
          "DISPATCH_INFO CALL UNIT BOX ADDRCITY PLACE PLACE/SDS! INFO/N+");
    setupSpecialStreets("TJ BRIDGE");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@co.cal.md.us";
  }
  
  private static final Pattern DELIM = Pattern.compile("[\\|\n]");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // The actual page format consists of different fields enclosed in square brackets.
    // But by the time it gets here those have all been converted to subject extensions
    // separated by pipe characters.
    body = subject + '|' + body;
    if (!parseFields(DELIM.split(body, -1), data)) return false;
    String call = CALL_CODES.getCodeDescription(data.strCode);
    if (call != null) data.strCall = call;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DISPATCH_INFO")) return new SkipField("Dispatch Info", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("BOX")) return new BoxField("Box +(.*)|", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CALL_PRIORITY_PTN = Pattern.compile("M Priority (\\d) +(.*)");
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PRIORITY_PTN.matcher(field);
      if (match.matches()) {
        data.strPriority = match.group(1);
        field = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "PRI CALL";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      DispatchProQAParser.parseProQAData(true, field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE INFO";
    }
  }
  
  private static CodeTable CALL_CODES = new StandardCodeTable();
}
