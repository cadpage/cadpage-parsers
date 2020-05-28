package net.anei.cadpage.parsers.SC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class SCRichlandCountyParser extends FieldProgramParser {
 
  public SCRichlandCountyParser() {
    super("RICHLAND COUNTY", "SC",
          "Address:ADDR! Problem:CALL! Facility:PLACE MapGrid:MAP");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "2002000004,@alert.active911.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.equals("911 Page") && body.startsWith("Comment:")) {
      data.strCall = "GENERAL ALERT";
      data.strPlace = body.substring(9).trim();
      return true;
    }
    
    // If the regular message parser handles this all is well
    body = body.replace(" MapGrid ", " MapGrid:");
    if (!super.parseMsg(body, data)) {
      
      // If not, see if we can get this through a general type parser
      // Which will only accept it caller has identified this as a dispatch page
      if (!isPositiveId()) return false;
      
      parseAddress(StartType.START_CALL, FLAG_IGNORE_AT | FLAG_NO_IMPLIED_APT, body, data);
      if (data.strAddress.length() == 0) return false;
      data.strSupp = getLeft();
    }
    
    // Fix mistyped address
    data.strAddress = data.strAddress.replace("Hardscrabble", "Hard Scrabble");
    data.strAddress = data.strAddress.replace("HARDSCRABBLE", "HARD SCRABBLE");
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Pattern CALL_UNIT_PTN = Pattern.compile("(.*) ([A-Z]+\\d+(?:,[A-Z0-9,]+)?)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_UNIT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strUnit = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL UNIT";
    }
  }
  
  private static final Pattern I26_PTN = Pattern.compile("I ?26 MM (\\d++)(?: [EW])?");
  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = address.toUpperCase();
    Matcher match = I26_PTN.matcher(address);
    if (match.matches()) address = "I26 MM " + match.group(1);
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "I26 MM 100",                           "+34.116077,-81.192335",
      "I26 MM 101",                           "+34.104440,-81.180370",
      "I26 MM 102",                           "+34.093455,-81.168544",
      "I26 MM 103",                           "+34.082634,-81.157314",
      "I26 MM 104",                           "+34.071359,-81.146058",
      "I26 MM 105",                           "+34.060407,-81.135084",
      "I26 MM 106",                           "+34.049482,-81.123124",
      "I26 MM 107",                           "+34.038075,-81.111919",
      "I26 MM 91",                            "+34.179152,-81.325758",
      "I26 MM 92",                            "+34.172983,-81.310029",
      "I26 MM 93",                            "+34.167533,-81.294065",
      "I26 MM 94",                            "+34.161608,-81.278161",
      "I26 MM 95",                            "+34.155612,-81.262177",
      "I26 MM 96",                            "+34.149835,-81.246734",
      "I26 MM 97",                            "+34.142816,-81.230906",
      "I26 MM 98",                            "+34.135915,-81.215648",
      "I26 MM 99",                            "+34.126521,-81.203159"
     
  });
}
