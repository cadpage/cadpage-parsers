package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHJacksonCountyParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile("(?<! )/(?! )");
  private static final String REGEX_TIME_HM = "\\d{2}:\\d{2}";
  
  public OHJacksonCountyParser() {
    super("JACKSON COUNTY", "OH",
           "CALL ADDR/Z+? APT TIME! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "alerts@jacksonems.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if(!subject.startsWith("Dispatch Alert")) return false;
    
    // Remove the "RC:" if the body starts with it
    if(body.startsWith("RC:")) {
      body = body.substring(3);     
    }

    // Split fields by special delimiter
    String[] fields = DELIM.split(body);
    
    return parseFields(fields, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField(REGEX_TIME_HM, true);
    return super.getField(name);
  }
  
  private static final Pattern PTN_PENN = Pattern.compile("\\bPENN\\b");
  @Override
  public String adjustMapAddress(String sAddress) {
    
    // Replace the single word "PENN" with "PENNSYLVANIA" to help with Google mapping
    return PTN_PENN.matcher(sAddress).replaceAll("PENNSYLVANIA");
  }
}
