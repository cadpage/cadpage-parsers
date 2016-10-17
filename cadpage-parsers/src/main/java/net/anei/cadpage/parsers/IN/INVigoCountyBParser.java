package net.anei.cadpage.parsers.IN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

/**
 * Vigo County, IN (B)
 */
public class INVigoCountyBParser extends DispatchSPKParser {
  
  private static final Pattern HLF_PATTERN = Pattern.compile("\\bHLF\\b", Pattern.CASE_INSENSITIVE);
  
  public INVigoCountyBParser() {
    super("VIGO COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "VigoCoCAD@ipsc.in.gov";
  }
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    
    // Turn HLF -> 1/2
    data.strAddress = HLF_PATTERN.matcher(data.strAddress).replaceAll("1/2");
    return true;
  }
}
