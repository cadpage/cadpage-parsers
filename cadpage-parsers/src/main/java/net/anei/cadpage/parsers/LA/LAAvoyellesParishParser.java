package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

public class LAAvoyellesParishParser extends SmartAddressParser {

  public LAAvoyellesParishParser() {
    super(CITY_LIST, "AVOYELLES PARISH", "LA");
    setFieldList("CALL ADDR APT CITY ST ID INFO");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "LAKE PEARL",
        "ST PHILLIP"
   );
  }

  @Override
  public String getFilter() {
    return "avoyelles911@pagingpts.com";
  }
  
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*?), +([A-Z]{2})(?: +(\\d{5}))?");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CAD Page for EventID:")) return false;
    data.strCallId = subject.substring(21).trim();
    
    int pt = body.indexOf(data.strCallId+' ');
    if (pt < 0) return false;
    data.strSupp = body.substring(pt+data.strCallId.length()+1).trim();
    body = body.substring(0, pt).trim();
    
    String zip = null;
    Matcher match = ADDR_ST_ZIP_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strState = match.group(2);
      zip = match.group(3);
    }
    
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_START_FLD_NO_DELIM | FLAG_ANCHOR_END, body, data);
    if (zip != null && data.strCity.length() == 0) data.strCity = zip;
    
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "BRUSH FIRE",
      "CRASH W/INJURY",
      "FIRE (AUTOMOBILE)",
      "FIRE CALL (OTHER)",
      "FIRE (STRUCTURE)",
      "INFORMATIONAL",
      "MEDICAL EMERGENCY"
  );
  
  private static final String[] CITY_LIST = new String[]{
      
      // Cities
      "BUNKIE",
      "MARKSVILLE",

      // Towns
      "COTTONPORT",
      "EVERGREEN",
      "MANSURA",
      "SIMMESPORT",

      // Villages
      "HESSMER",
      "MOREAUVILLE",
      "PLAUCHEVILLE",
      
      // DMA
      "Alexandria",

      // Census-designated places
      "BORDELONVILLE",
      "CENTER POINT",
      "FIFTH WARD"
  };
}
