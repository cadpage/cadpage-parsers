package net.anei.cadpage.parsers.GA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class GAHoustonCountyParser extends DispatchOSSIParser {
  
  private static final Pattern MASTER = Pattern.compile("HOUSTON COUNTY 911:? \\((.*?)\\) (CAD:.*)", Pattern.DOTALL);
  
  public GAHoustonCountyParser() {
    super(CITY_CODES, "HOUSTON COUNTY", "GA",
           "FYI? CALL ADDR ( X/Z UNIT | UNIT | X? ) CITY? ( DATETIME2! | DATETIME1 ) INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "4702193729,cad@houstoncountye911.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Seriously message up reformatting needs to be reversed :(
    // They duplicated the message text and truncated both of parts at different
    // places.  We will pick whichever is longer
    Matcher match = MASTER.matcher(body);
    if (match.matches()) {
      subject = "CAD:" + match.group(1).trim();
      body = match.group(2).trim();
    }
    if (subject.length() > body.length()) body = subject;
    
    // Regular parser can do the rest
    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name){
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+", true);
    if (name.equals("DATETIME1")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("DATETIME2")) return new SkipField("\\d\\d/\\d\\d/\\d{4} +\\d\\d", true);
    return super.getField(name);
  }
      
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BON", "BONAIRE",
      "BYR", "BYRON",
      "PRY", "PERRY",
      "WR",  "WARNER ROBINS"
  });
}