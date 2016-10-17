package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYOrleansCountyParser extends MsgParser {
  
  private static final Pattern CODE_PATTERN = Pattern.compile("\\b\\d{1,2}-?[A-Za-z]-?\\d\\b");
  private static final Pattern NIA_ORL_CTYLINE = Pattern.compile("\\bNIA ORL CTYLINE RD\\b", Pattern.CASE_INSENSITIVE);
  
  public NYOrleansCountyParser() {
    super("ORLEANS COUNTY", "NY");
    setFieldList("CALL ADDR CITY INFO CODE");
  }
  
  @Override
  public String getFilter() {
    return "ocdispatch@orleansny.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    String[] flds = body.split(";");
    data.strCall = flds[0].trim();
    
    if (flds.length <= 1) return false;
    String fld = flds[1].trim();
    int pt = fld.lastIndexOf(' ');
    if (pt < 0) return false;
    String sAddr = fld.substring(0,pt);
    sAddr = NIA_ORL_CTYLINE.matcher(sAddr).replaceAll("COUNTY LINE RD");
    parseAddress(sAddr, data);
    data.strCity = CITY_CODES.getProperty(fld.substring(pt+1));
    if (data.strCity == null) return false;
    
    // Anything else is extra info
    for (int ndx = 2; ndx < flds.length; ndx++) {
      fld = flds[ndx].trim();
      if (fld.length() == 0) continue;
      data.strSupp = append(data.strSupp, " / ", fld);
    }
    
    // There might be a type code buried in there, see if we can find it
    Matcher match = CODE_PATTERN.matcher(data.strSupp);
    if (match.find()) {
      data.strCode = data.strSupp.substring(match.start(), match.end());
      String part1 = data.strSupp.substring(0, match.start()).trim();
      String part2 = data.strSupp.substring(match.end()).trim();
      if (part2.length() == 0) data.strSupp = part1;
      else if (part1.length() == 0) data.strSupp = part2;
      else data.strSupp = part1 + " " + part2;
    }
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "VAL", "ALBION", 
      "TAL", "ALBION", 
      "BAR", "ALBION", 
      "TGN", "ALBION", 
      "TCR", "CARLTON",
      "TGP", "GASPORT",  
      "HAR", "HARTLAND",
      "VHL", "HOLLEY", 
      "TMR", "HOLLEY", 
      "TCL", "HOLLEY",
      "TKN", "KENDALL", 
      "VLD", "LYNDONVILLE", 
      "YAT", "LYNDONVILLE",
      "VMD", "MEDINA", 
      "TRW", "MEDINA", 
      "TSH", "MEDINA",
      "MDL", "MIDDLEPORT",
      "TYA", "YATES"
  });
}
