package net.anei.cadpage.parsers.NH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NHMerrimackCountyParser extends FieldProgramParser {
  
  public NHMerrimackCountyParser() {
    super("MERRIMACK COUNTY","NH",
           "( EVENT:SKIP TIME:DATETIME! TYPE:CALL! LOC:ADDR! TXT:INFO+ | " +
             "TYPE:CALL TIME:DATETIME! LOC:ADDR! CITY:CITY! INFO+ | " +
             "TIME:DATETIME! CITY:CITY! LOC:ADDR! TYPE:CALL! TXT:INFO+ | " +
             "CALL DATETIME ADDR CITY! INFO+ )");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@newlondonpd.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD")) data.strSource = subject;
    body = body.replace("\nLOCATION:", "\nLOC:");
    return parseFields(body.split("\n"), 4, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
