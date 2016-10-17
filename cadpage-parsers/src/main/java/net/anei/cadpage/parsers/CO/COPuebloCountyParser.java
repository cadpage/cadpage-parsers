package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class COPuebloCountyParser extends SmartAddressParser {
  
  private static final Pattern TIME_MARK = Pattern.compile("^\\d\\d:\\d\\d[A-Z][A-Z] \\d\\d/\\d\\d ");
  private static final Pattern DELIM = Pattern.compile(" *(?:\\.{2,}|;) *");
    
  public COPuebloCountyParser() {
    super("PUEBLO COUNTY", "CO");
    setFieldList("CALL ADDR INFO");
  }
  
  @Override
  public String getFilter() {
    return "surepage@wdsl.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Page")) return false;
    Matcher match = TIME_MARK.matcher(body);
    if (match.find()) body = body.substring(match.end()).trim();
    
    String[] flds = DELIM.split(body);
    int fldCnt = flds.length;
    if (flds[fldCnt-1].length() <= 4) fldCnt--;
    if (fldCnt == 0) return false;
    
    for (int ndx = 0; ndx < fldCnt; ndx++) {
      String fld = flds[ndx];
      if (data.strAddress.length() == 0) {
        if (data.strCall.length() == 0) {
          parseAddress(StartType.START_CALL, fld, data);
        } else {
          parseAddress(StartType.START_ADDR, fld, data);
        }
        data.strSupp = getLeft();
      }
      
      else {
        data.strSupp = append(data.strSupp, " / ", fld);
      }
    }
    return true;
  }
}
