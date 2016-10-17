package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Washington County, OR
 * Also Clackamas County
 */
public class ORLakeOswegoParser extends FieldProgramParser {
  
  private static final Pattern PTN_UNIT_CODE_CALL = Pattern.compile("([A-Z0-9]{3,5}) - ([A-Z0-9/]{3,4}) \\((.*)\\)");
  
  public ORLakeOswegoParser() {
    this("LAKE OSWEGO", "OR");
  }
  
  public ORLakeOswegoParser(String defCity, String defState) {
    super(ORWashingtonCountyAParser.CITY_CODES, defCity, defState,
           "UNIT CODE CALL ADDRCITY SRC! MAP:MAP! btwn:X? UNK PLACE:PLACE? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "wccca@wccca.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    Matcher messageHeader = PTN_UNIT_CODE_CALL.matcher(body);
    if(!messageHeader.find()) return false;
    
    body = body.substring(messageHeader.end());           // Remove Unit, Code, and Call fields
    body = body.replace("-- ", "- PLACE:");               // A "--" denotes a Place name, add label
    body = body.replace(" btwn ", " btwn:");                // "btwn " denotes a cross street, turn into label
    body = body.replace(" high xst:", " btwn:");            // "high xst:" takes the place of the cross street.
    String[] fields = body.split("- ");                   // The rest of the fields are delimited by "- "
    
    // Create Array to hold Matched fields and regular fields.  Then pass
    // the entire array to parse Fields.
    String[] allFields = new String[fields.length+3];
    allFields[0] = messageHeader.group(1);
    allFields[1] = messageHeader.group(2);
    allFields[2] = messageHeader.group(3).trim();
    System.arraycopy(fields, 0, allFields, 3, fields.length); 
    
    return parseFields(allFields, data);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if(field.endsWith("-")) {
        field = field.substring(0, field.length()-2);
      }
      data.strSupp = field;
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{2,3}");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
}
