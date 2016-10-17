  package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Bucks County, PA
 */

public class PABucksCountyBParser extends PABucksCountyBaseParser {
  
  public PABucksCountyBParser() {
    super();
  }
  
  @Override
  public String getFilter() {
    return "8276,@bnn.us,iamresponding.com,Bucks RSAN,@alert.bucksema.org,777,@co.bucks.pa.us,@buckscounty.org,@buckscounty.gov";
  }
  
  private static final Pattern SRC_PTN = Pattern.compile("TO INT1 FROM [A-Z0-9]+ *: *\n?(.*?)(?=\n)");
  private static final Pattern SRC2_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d +Message Re-routed from terminal: [A-Z0-9]+ *\nTo: INT1\n");
  private static final Pattern COVER_PTN = Pattern.compile("COVER ASSIGNMENT\n(.*)\nFor Inc ([A-Z0-9]+) +at (\\d\\d:\\d\\d:\\d\\d)\nText: (.*)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    do {
      Matcher match = SRC_PTN.matcher(body);
      if (match.find()) {
        data.strSource = match.group(1).trim();
        body = body.substring(match.end()).trim();
        break;
      }
      match = SRC2_PTN.matcher(body);
      if (match.find()) {
        body = body.substring(match.end()).trim();
        break;
      }
      return false;
    } while (false);
    
    int pt = body.indexOf("\n*****");
    if (pt >= 0) body = body.substring(0,pt).trim();

    
    Matcher match = COVER_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL ID TIME INFO");
      data.strCall = match.group(1).trim();
      data.strCallId = match.group(2).trim();
      data.strTime = match.group(3).trim();
      data.strSupp = match.group(4).trim();
      return true;
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
