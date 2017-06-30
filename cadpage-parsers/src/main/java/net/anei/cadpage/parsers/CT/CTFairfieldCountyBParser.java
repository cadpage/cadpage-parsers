package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class CTFairfieldCountyBParser extends SmartAddressParser {

  public CTFairfieldCountyBParser() {
    super(CTFairfieldCountyParser.CITY_LIST, "FAIRFIELD COUNTY", "CT");
    setFieldList("SRC ADDR APT CITY CALL TIME");
  }
  
  @Override
  public String getFilter() {
    return "swrcc@dmsct.net";
  }

  private static Pattern MASTER = Pattern.compile("(?:MEMS:([^\\|]*?)\\| )?(.*?) - (.*?) --(?:Disp |DISP |CMED)@ (\\d{2}:\\d{2})");
  
  @Override protected boolean parseMsg(String subject, String body, Data data) {
    
    // Process general alerts
    if (subject.equals("Message from SWRCC")) {
      setFieldList("INFO");
      data.strCall = "GEN_ALERT";
      data.strSupp = body;
      return true;
    }
    
    if (!subject.contains(":")) data.strSource = subject;
    
    
    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;
    parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, mat.group(2).trim(), data);
    data.strCall = append(getOptGroup(mat.group(1)), " - ", mat.group(3).trim());
    data.strTime = mat.group(4).trim();
    return true;
  }
  
}
