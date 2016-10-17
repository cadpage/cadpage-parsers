package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Solano County, CA (C)
 */
public class CASolanoCountyCParser extends FieldProgramParser {
  
  public CASolanoCountyCParser() {
    super("SOLANO COUNTY", "CA",
          "( MUTUAL_AID_CALL ADDR ( CITY PLACE MAP1 INFO UNIT ID DATE/d TIME! | INFO UNIT ID DATE/d TIME! ) " +
          "| CALL_PRI ADDR CITY ( X X/Z? MAP1 | PLACE X X/Z? MAP1 | PLACE MAP1 ) MAP2/D+? INFO+? UNIT ID DATE/d TIME! )");
    setupSpecialStreets("PLAZA DE ORO",
                        "MARINA VISTA",
                        "MILITARY EAST",
                        "MILITARY WEST");
  }
  
  @Override
  public String getFilter() {
    return "benicia@dapage.net";
  }
  
  private static final Pattern PAMA_PATTERN = Pattern.compile("(.*)INTERSECTN");
  @Override
  public String adjustMapAddress(String field) {
    Matcher m = PAMA_PATTERN.matcher(field);
    if (m.matches()) field = m.group(1).trim();
    return super.adjustMapAddress(field);
  }
  
  private static final Pattern SUBJECT_PATTERN = Pattern.compile("50\\d{2}(.*)dispatch");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher m = SUBJECT_PATTERN.matcher(subject);
    if (!m.matches()) return false;
    return parseFields(body.split("/"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MUTUAL_AID_CALL")) return new CallField("MUTUAL AID.*", true);
    if (name.equals("CALL_PRI")) return new CallPriorityField();
    if (name.equals("MAP1")) return new MapField("F\\d+", true);
    if (name.equals("MAP2")) return new MapField("\\d{1,2}[A-Z]?", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z]{1,3}\\d{1,3}(?: +[A-Z]{1,3}\\d{1,3})*", true);
    if (name.equals("ID")) return new IdField("\\d{9}", true);
    if (name.equals("DATE")) return new DateField("\\d{2}-\\d{2}-\\d{2}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern CALL_PRI_PATTERN = Pattern.compile("(.*)- PRIORITY(.*)");
  private class CallPriorityField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = CALL_PRI_PATTERN.matcher(field);
      if (m.matches()) {
        field = m.group(1).trim();
        data.strPriority = m.group(2).trim();
      }
      data.strCall = field;
    }
      
    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" PRI";
    }
  }
}
