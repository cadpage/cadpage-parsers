package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

/**
 * Marin County, CA
 */
public class CAMarinCountyParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("(\\w+): (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) From: CNSL (\\d):(.*?)\\[(\\d+) ?\\](.*?)(?:,([A-Z]+))? btwn (.*)");
  
  public CAMarinCountyParser() {
    super("MARIN COUNTY", "CA");
    setFieldList("SRC DATE TIME PRI CALL ID ADDR ADDR CITY X");
  }
  
  @Override
  public String getFilter() {
    return "CAD@marinsheriff.org";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strSource = match.group(1);
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    data.strPriority = match.group(4);
    data.strCall = match.group(5).trim();
    data.strCallId = match.group(6);
    parseAddress(match.group(7).trim(), data);
    String city = match.group(8);
    if (city != null) data.strCity = convertCodes(city, CITY_CODES);
    data.strCross = match.group(9).trim();
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "MVP", "MILL VALLEY",
      "SAS", "SAUSALITO",
      "SBY", "",        // Belvedere Tiburon
      "TAM", "TAMALPAIS-HOMESTEAD VALLEY"
      
  });
}
