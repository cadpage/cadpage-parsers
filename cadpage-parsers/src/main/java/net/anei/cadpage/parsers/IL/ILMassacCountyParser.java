
package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Massac County, IL
 */
public class ILMassacCountyParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile("[;\n]");

  public ILMassacCountyParser() {
    super("MASSAC COUNTY", "IL",
          "SRC_DATE_TIME CALL ADDR CITY INFO!");
  }
    
  @Override
  public String getFilter() {
    return "page@joppafd.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), 5, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC_DATE_TIME")) return new MySrcDateTimeField();
    return super.getField(name);
  }

  private static Pattern SRC_DATE_TIME_PTN = Pattern.compile("(.*?) Page Received at (\\d\\d:\\d\\d:\\d\\d) on (\\d\\d/\\d\\d/\\d\\d)");
  private class MySrcDateTimeField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = SRC_DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = match.group(1).trim();
      data.strTime = match.group(2);
      data.strDate = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "SRC TIME DATE";
    }
  }
}
