package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.Message;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

/**
 * Gloucester County, NJ (version A)
 */
public class NJGloucesterCountyAParser extends DispatchProphoenixParser {
  
  private static final Pattern FROM_ADDR_PTN = Pattern.compile("GC RSAN #(\\d+)");
  private static final Pattern SPECIAL_TRAIL_PTN = Pattern.compile(";.{2}$");
  
  private String fromAddress;
  
  public NJGloucesterCountyAParser() {
    super(NJGloucesterCountyParser.CITY_CODES, "GLOUCESTER COUNTY", "NJ");
  }
 
  @Override
  public String getFilter() {
    return "gccad@co.gloucester.nj.us,777,@private.gloucesteralert.com,12101,411912";
  }
  
  @Override
  protected Data parseMsg(Message msg, int parseFlags) {
    fromAddress = msg.getFromAddress();
    return super.parseMsg(msg, parseFlags);
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    
    // THere are some weird text options presumably introduced by forwarding services
    if (body.startsWith("Fwd:")) body = body.substring(4).trim();
    if (body.endsWith("=")) body = body.substring(0,body.length()-1).trim();
    
    Matcher match = FROM_ADDR_PTN.matcher(fromAddress);
    if (match.matches()) {
      body = "GC ALERT (#" + match.group(1) + ") " + body;
      match = SPECIAL_TRAIL_PTN.matcher(body);
      if (match.find()) body = body.substring(0,match.start()).trim();
    }
    
    if (!body.contains("\n")) body = body.replace("} ", "}\n");
    return super.parseMsg(body, data);
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("ROWAN")) city = "GLASSBORO";
    return city;
  }
}
