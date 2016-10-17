package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class OHGenevaParser extends SmartAddressParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z]{4}");
  private static final Pattern TRAILER_PTN = Pattern.compile(" GENEVA(?: +OH(?:IO)?)?$", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDRESS_BRK_PTN = Pattern.compile("\\bFOR\\b", Pattern.CASE_INSENSITIVE);

  public OHGenevaParser() {
    super("GENEVA", "OH");
    setFieldList("SRC CALL ADDR APT INFO");
  }
  
  @Override
  public String getFilter() {
    return "Genevafiredepartment@genevaohio.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!SUBJECT_PTN.matcher(subject).matches()) return false;
    data.strSource = subject;
    
    Matcher match = TRAILER_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();
    
    parseAddress(StartType.START_CALL, body, data);
    String left = getLeft();
    if (left.length() == 0) {
      match = ADDRESS_BRK_PTN.matcher(data.strAddress);
      if (match.find()) {
        left = data.strAddress.substring(match.start()).trim();
        data.strAddress = data.strAddress.substring(0,match.start()).trim();
      }
    }
    if (data.strCall.length() == 0) {
      data.strCall = left;
    } else {
      data.strSupp = left;
    }
    return true;
  }
}
