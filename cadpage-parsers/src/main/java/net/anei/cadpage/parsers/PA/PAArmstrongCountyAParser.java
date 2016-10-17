package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Armstrong County, PA
 */
public class PAArmstrongCountyAParser extends FieldProgramParser {

  // Marker is time and run number at end of message
  private static final Pattern MARKER_PATTERN1 = Pattern.compile(" +(\\d+) +(\\d\\d:\\d\\d)$");
  private static final Pattern MARKER_PATTERN2 = Pattern.compile(" +(\\d{1,6}) (\\d\\d:\\d\\d)(?: (\\d{10}))?(?: +([A-Z0-9]+))?$");
  
  public PAArmstrongCountyAParser() {
    super("ARMSTRONG COUNTY", "PA",
          "CALL PLACE? ADDR/Z ( X CITY END | CITY/Z X/Z! END )");
  }
  
  @Override
  public String getFilter() {
    return "911Dispatch@co.armstrong.pa.us,2183500107";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Someone is adding some clever post parsing that we need to undo
    int pt = body.indexOf(" Unformatted Message: ");
    if (pt >= 0) {
      subject = "Dispatch";
      body = body.substring(pt+22).trim();
      body = stripFieldEnd(body, " stop");
    }
    
    // There are two different formats we process.
    // First look for the current one
    if (subject.equals("Dispatch")) {
      Matcher match = MARKER_PATTERN1.matcher(body);
      if (match.find()) {
        data.strCallId = match.group(1);
        data.strTime = match.group(2);
        body = body.substring(0,match.start());
        
        // There are two versions of this format.
        // Newer one uses // as field delimiters
        String[] flds = body.split("//");
        if (flds.length >= 4) {
          return parseFields(flds, data);
        }
        
        // Older one has same fields parsed by hand
        setFieldList("CALL PLACE ADDR X");
        
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_CROSS_FOLLOWS | FLAG_NO_IMPLIED_APT, body, data);
        data.strCross = getLeft();
        
        pt = data.strCall.indexOf("  ");
        if (pt >= 0) {
          data.strPlace = data.strCall.substring(pt+2).trim();
          data.strCall = data.strCall.substring(0,pt);
        }
        return true;
      }
    }
    
    
    // No go, see if this matches an older format
    Matcher match = MARKER_PATTERN2.matcher(body);
    if (match.find()) {
      setFieldList("ADDR X PLACE ID TIME PHONE CALL");
      
      data.strCallId = match.group(1);
      data.strTime = match.group(2);
      data.strPhone = getOptGroup(match.group(3));
      data.strCall = getOptGroup(match.group(4));
      body = body.substring(0,match.start());
      
      if (data.strCall.length() == 0) {
        Parser p = new Parser(body);
        data.strCall = p.getLast(' ');
        body = p.get();
      }
      
      
      // We need to call the smart parser twice, once for the real address
      // and a second time to get the cross streets (which look like intersections)
      
      body = body.replace(",", "/");
      parseAddress(StartType.START_ADDR, body, data);
      body = getLeft();
      do {
        if (body.startsWith("/")) body = body.substring(1).trim();
        Result result2 = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, body);
        if (result2.isValid()) {
          String oldCross = data.strCross;
          data.strCross = "";
          result2.getData(data);
          data.strCross = append(oldCross, " / ", data.strCross);
          body = result2.getLeft();
        }
      } while (body.startsWith("/"));
      
      data.strPlace = body;
  
      return true;
    }
    
    // Not luck on either format
    return false;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " ID TIME";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new CrossField(".*(?:/| and ).*");
    return super.getField(name);
  }
  
  private static final Pattern CALL_CH_PLACE_PTN = Pattern.compile("(.*?) *\\b((?:EMS )?TAC ?\\d|EMS ?\\d|ET ?\\d|CW ?\\d|FIRE FT\\d|FRE \\d)\\b *(.*?)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CH_PLACE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strChannel = match.group(2);
        data.strPlace = match.group(3);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL CH PLACE";
    }
  }
  
  private static final Pattern CITY_BORO_PTN = Pattern.compile("(.*?) +BORO", Pattern.CASE_INSENSITIVE);
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_BORO_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
}
