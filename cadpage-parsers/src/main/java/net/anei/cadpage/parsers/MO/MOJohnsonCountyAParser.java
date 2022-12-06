package net.anei.cadpage.parsers.MO;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;




public class MOJohnsonCountyAParser extends FieldProgramParser {
 
  public MOJohnsonCountyAParser() {
    super(CITY_LIST, "JOHNSON COUNTY", "MO", 
          "SRC CALL ADDR CITY X/Z+? DATETIME! END");
  }
  
  @Override
  public String getFilter() {
    return "@joco911.org,93001";
  }
  
  private static final Pattern MARKER = Pattern.compile("\\b(?:Dispatch: +)?(\\d{1,2}/\\d{1,2}/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)\\b");
  private static final Pattern SRC_PTN = Pattern.compile("(JCSO|JCF|JCAD) +");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    body = body.replace("=\n", "");
    
    // New format
    if (body.startsWith("Dispatch,")) {
      subject = "Dispatch";
      body = body.substring(9).trim();
    }
    
    if (subject.equals("Dispatch")) {
      String[] flds = body.split(";");
      if (flds.length >= 5) return parseFields(flds, data);
      
      setFieldList("CALL ADDR APT INFO");
      for (String part : body.split(",")) {
        part = part.trim();
        if (data.strAddress.length() == 0) {
          Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, part);
          if (res.isValid()) {
            res.getData(data);
            continue;
          }
          data.strCall = append(data.strCall, ", ", part);
        } else {
          data.strSupp = append(data.strSupp, ", ", part);
        }
      }
      return data.strAddress.length() > 0;
    }
    
    do {
      if (subject.contains("911 Page")) break;
      
      if (body.startsWith("911 Page / ")) {
        body = body.substring(11).trim();
        break;
      }
      
      if (body.startsWith("911 Page,")) {
        body = body.substring(9).trim();
        break;
      }
      
      return false;
    } while (false);
    
    setFieldList("SRC CALL ADDR APT CITY DATE TIME");

    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    if (match.start() == 0) {
      body = body.substring(match.end()).trim();
    } else if (match.end() == body.length() ||
               body.substring(match.end()).trim().equals(match.group())) {
      body = body.substring(0,match.start());
    } else return false;
    
    match = SRC_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strSource = match.group(1);
      body = body.substring(match.end());
    }
    
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, body, data);
    if (data.strCity.startsWith("JOHNSON COUNTY")) data.strCity = "";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "JOHNSON COUNTY",
    "JOHNSON COUNTY MO"
  };
}