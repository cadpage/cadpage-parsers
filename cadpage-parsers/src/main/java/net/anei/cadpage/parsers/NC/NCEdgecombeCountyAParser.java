package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCEdgecombeCountyAParser extends FieldProgramParser {
  
  public NCEdgecombeCountyAParser() {
    super(NCEdgecombeCountyParser.CITY_LIST, "EDGECOMBE COUNTY", "NC",
          "( CITY | ADDR PLACE EMPTY CITY ) EMPTY EMPTY EMPTY EMPTY EMPTY CALL EMPTY EMPTY UNIT! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@co.edgecombe.nc.us";
  }
  
  private static final Pattern MARKER1 = Pattern.compile("Edgecombe(?:911|Central):(\\d{9})\\s+");
  private static final Pattern MARKER2 = Pattern.compile("(\\d{9}): +");
  private static final Pattern CALL_CODE_UNIT_PTN = Pattern.compile("(.*) CODE (\\d) (.*)");
  private static final Pattern CALL_UNIT_PTN = Pattern.compile("(.*?) ([A-Z]*\\d[ ,A-Z0-9]*)");
  
  @Override
  public boolean parseMsg(String body, Data data) {

    boolean bad = false;
    Matcher match = MARKER1.matcher(body);
    if (!match.lookingAt()) {
      match = MARKER2.matcher(body);
      if (!match.lookingAt()) bad = true;
    }
    
    if (!bad) {
      data.strCallId = match.group(1);
      body = body.substring(match.end());
    }
    
    String[] flds =  body.split("\n");
    if (flds.length >= 10) return parseFields(flds, data);
    
    if (bad) return false;
    
    setFieldList("ADDR APT CITY CALL PRI UNIT INFO");
    int pt = body.indexOf(" Medical:");
    if (pt >= 0) {
      data.strSupp = body.substring(pt+1);
      body = body.substring(0,pt).trim();
    }
    parseAddress(StartType.START_ADDR, body.replace(" @ ", " / ").replace("//", "/"), data);
    body = getLeft();
    if (body.length() == 0) return false;
    
    // If there is a priority field separating the call description and units
    // things are easy
    match = CALL_CODE_UNIT_PTN.matcher(body);
    if (match.find()) {
      data.strCall = match.group(1).trim();
      data.strPriority = match.group(2);
      data.strUnit = match.group(3).trim();
    }
    
    // Else look for a unit group following the description
    else if ((match = CALL_UNIT_PTN.matcher(body)).matches()) {
      data.strCall = match.group(1).trim();
      data.strUnit = match.group(2);
    }
    
    // Otherwise everything goes in call description
    else {
      data.strCall = body;
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return CH_PTN.matcher(addr).replaceAll("CHURCH");
  }
  private static final Pattern CH_PTN = Pattern.compile("\\bCH\\b");
}
