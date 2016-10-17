package net.anei.cadpage.parsers.DE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchChiefPagingParser;


public class DENewCastleCountyBParser extends DispatchChiefPagingParser {
  
  private static final Pattern EMD_PTN = Pattern.compile(" - EMD:([A-Z0-9]*)$", Pattern.CASE_INSENSITIVE);
  
  public DENewCastleCountyBParser() {
    super(CITY_LIST, "NEW CASTLE COUNTY", "DE");
  }

  @Override
   protected boolean parseMsg(String subject, String body, Data data) {
    
    // Drop calls from Deepwater, NJ
    if (body.contains(" Caller:")) return false;
    
    int pt = body.indexOf(" - Notes:PROQA");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = EMD_PTN.matcher(body);
    if (match.find()) {
      data.strCode = match.group(1);
      body = body.substring(0,match.start()).trim();
    }
    
    body = body.replace("Dead - End", "Dead End");
    return  super.parseMsg(subject,  body, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " CODE";
  }
  
  private static String[] CITY_LIST = new String[]{
    "NEW CASTLE",
    
    "ARDENCROFT",
    "ARDENTOWN",
    "BELLEFONTE",
    "CLAYTON",
    "DELAWARE CITY",
    "ELSMERE",
    "MIDDLETOWN",
    "NEW CASTLE",
    "NEWARK",
    "NEWPORT",
    "ODESSA",
    "SMYRNA",
    "TOWNSEND",
    "WILMINGTON",
    "BEAR",
    "BROOKSIDE",
    "CLAYMONT",
    "COLLINS PARK",
    "CHRISTIANA",
    "EDGEMOOR",
    "GLASGOW",
    "GREENVILLE",
    "HOCKESSIN",
    "HOLLY OAK",
    "MARSHALLTON",
    "MINQUADALE",
    "MONTCHANIN",
    "NORTH STAR",
    "OGLETOWN",
    "PIKE CREEK",
    "ROCKLAND",
    "ST. GEORGES",
    "STANTON",
    "WILMINGTON MANOR",
    "WINTERTHUR",
    "WINTERSET"
  };
}


