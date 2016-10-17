package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchCiscoParser;

/**
 * Vigo County, IN
 */
public class INVigoCountyAParser extends DispatchCiscoParser {
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "WTH", "WEST TERRE HAUTE"
  });
  
  private static final Pattern HLF_PATTERN = Pattern.compile("\\bHLF\\b", Pattern.CASE_INSENSITIVE);
  
  public INVigoCountyAParser() {
    super(CITY_CODES, "VIGO COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "37284,cisco.paging@vigocounty.in.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    
    // Turn HLF -> 1/2
    data.strAddress = HLF_PATTERN.matcher(data.strAddress).replaceAll("1/2");
    return true;
  }
}
