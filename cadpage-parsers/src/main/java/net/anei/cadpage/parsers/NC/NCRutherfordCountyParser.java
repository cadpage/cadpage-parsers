package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCRutherfordCountyParser extends FieldProgramParser {
  
  public NCRutherfordCountyParser() {
    super("RUTHERFORD COUNTY", "NC",
           "Location:ADDR! APT/ROOM:APT? City:CITY! Call_Type:CALL! Line11:INFO? Units:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "paging@rutherfordcountync.gov";
  }
  
  private static final Pattern KEYWORD_DELIM = Pattern.compile("(?<=Location|APT/ROOM|City|Call Type|Line11|Units)[^*]");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    String[] parts = subject.split("\\|");
    subject = parts[parts.length-1];
    do {
      if (subject.equals("Paging")) break;
      
      if (body.startsWith("Paging:")) {
        body = body.substring(7).trim();
        break;
      }
      if (subject.endsWith("PageGate")) break;
      return false;
    } while (false);
    
    body = body.replaceAll("\n", "");
    body = KEYWORD_DELIM.matcher(body).replaceAll(":");
    return super.parseFields(body.split("\\*"), data);
  }
}
