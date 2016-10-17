package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/*
 * Montgomery County, PA (version E)
 */
public class PAMontgomeryCountyEParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile(" \\*\\* ");
  
  public PAMontgomeryCountyEParser() {
    super(PAMontgomeryCountyParser.CITY_CODES, "MONTGOMERY COUNTY", "PA",
           "CALL ADDR CITY UNIT EMPTY X TIME EMPTY INFO ID DATE EMPTY EMPTY!");
  }
  
  @Override
  public String getFilter() {
    return "345pager@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Rule out A variant page
    if (body.startsWith("Dispatch **")) return false;
    
    if (body.endsWith("**")) body = body + ' ';
    body = body.replace('\n', ' ');
    return parseFields(DELIM.split(body, -1), 13, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d|", true);
    if (name.equals("ID")) return new IdField("E\\d+");
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d|", true);
    return super.getField(name);
  }
}
