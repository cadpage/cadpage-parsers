package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCEdgecombeCountyAParser extends FieldProgramParser {
  
  public NCEdgecombeCountyAParser() {
    super(NCEdgecombeCountyParser.CITY_LIST, "EDGECOMBE COUNTY", "NC",
          "( CITY | ADDR/S6 APT EMPTY CITY ) EMPTY EMPTY EMPTY INFO ( ROAD_CLOSED EMPTY CALL/SDS | EMPTY CALL EMPTY ) EMPTY UNIT! INFO/N+");
    removeWords("ROAD");
  }
  
  @Override
  public String getFilter() {
    return "@co.edgecombe.nc.us";
  }
  
  private static final Pattern MARKER1 = Pattern.compile("Edgecombe(?:911|Central):(\\d{9}\\b)?\\s*");
  private static final Pattern MARKER2 = Pattern.compile("(\\d{9}): +");
  private static final Pattern CALL_CODE_UNIT_PTN = Pattern.compile("(.*) CODE (\\d) (.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CALL_UNIT_PTN = Pattern.compile("(.*?) ([A-Z]*\\d[ ,A-Z0-9]*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CHANNEL_PTN = Pattern.compile("\\bEVT?(?: CH)? ?\\d+$", Pattern.CASE_INSENSITIVE);
  
  @Override
  public boolean parseMsg(String body, Data data) {

    boolean bad = false;
    Matcher match = MARKER1.matcher(body);
    if (!match.lookingAt()) {
      match = MARKER2.matcher(body);
      if (!match.lookingAt()) bad = true;
    }
    
    if (!bad) {
      data.strCallId = getOptGroup(match.group(1));
      body = body.substring(match.end());
    }
    
    String[] flds =  body.split("\n");
    if (flds.length >= 10) {
      if (!parseFields(flds, data)) return false;
    }
    
    else {
      
      setFieldList("ADDR APT CH CITY CALL PRI UNIT INFO");
      int pt = body.indexOf(" Medical:");
      if (pt >= 0) {
        data.strSupp = body.substring(pt+1);
        body = body.substring(0,pt).trim();
      }
      body = stripFieldStart(body, ":");
      parseAddress(StartType.START_ADDR, body.replace(" @ ", " / ").replace("//", "/"), data);
      body = getLeft();
      if (body.length() == 0) return false;
      
      if (bad && data.strCity.length() == 0) return false;
      
      // If there is a priority field separating the call description and units
      // things are easy
      match = CALL_CODE_UNIT_PTN.matcher(body);
      if (match.find()) {
        data.strCall = match.group(1).trim();
        data.strPriority = match.group(2);
        data.strUnit = match.group(3).trim().trim();
      }
      
      // Else look for a unit group following the description
      else if ((match = CALL_UNIT_PTN.matcher(body)).matches()) {
        data.strCall = match.group(1);
        data.strUnit = match.group(2).trim();
      }
      
      // Otherwise everything goes in call description
      else {
        data.strCall = body;
      }
    }
    
    data.strUnit = data.strUnit.replace(' ', '_');
    
    if (data.strApt.length() > 0) {
      if (CHANNEL_PTN.matcher(data.strApt).matches()) {
        data.strChannel = data.strApt;
        data.strApt = "";
      }
    } else {
      match = CHANNEL_PTN.matcher(data.strAddress);
      if (match.find()) {
        data.strChannel = match.group();
        data.strAddress = stripFieldEnd(data.strAddress.substring(0,match.start()).trim(), ":");
      }
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram().replaceAll("APT", "APT CH");
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ROAD_CLOSED")) return new CallField("Road Closed", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CALL_PRI_PTN = Pattern.compile("(.*) CODE (\\d)", Pattern.CASE_INSENSITIVE);
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match =  CALL_PRI_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPriority = match.group(2);
      }
      super.parse(field,  data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL PRI";
    }
  }
  
  private static final Pattern INFO_PFX_PTN = Pattern.compile("Line\\d+= *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher  match = INFO_PFX_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return CH_PTN.matcher(addr).replaceAll("CHURCH");
  }
  private static final Pattern CH_PTN = Pattern.compile("\\bCH\\b");
}
