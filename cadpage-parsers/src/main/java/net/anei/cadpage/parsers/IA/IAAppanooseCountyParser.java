package net.anei.cadpage.parsers.IA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA47Parser;


public class IAAppanooseCountyParser extends DispatchA47Parser {
  
  public IAAppanooseCountyParser() {
    super("Dispatch", CITY_LIST, "APPANOOSE COUNTY", "IA", ".*");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@appanoosecountysheriff.org";
  }
  
  private static final Pattern FIX_ZIP_PTN = Pattern.compile(",IA +[\\d]{1,4}$");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Long alerts tend to get split up in the zip code.  If we only got a partial
    // alert, clean up any truncated zip codes.
    Matcher match = FIX_ZIP_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()+3);
    return super.parseMsg(subject, body, data);
  }

  private static final String[] CITY_LIST =new String[]{
    
    "// Cities",
    "CENTERVILLE",
    "CINCINNATI",
    "EXLINE",
    "MORAVIA",
    "MOULTON",
    "MYSTIC",
    "NUMA",
    "PLANO",
    "RATHBUN",
    "UDELL",
    "UNIONVILLE",

    "// Townships",
    "BELLAIR",
    "CALDWELL",
    "CHARITON",
    "DOUGLAS",
    "FRANKLIN",
    "INDEPENDENCE",
    "JOHNS",
    "LINCOLN",
    "PLEASANT",
    "SHARON",
    "TAYLOR",
    "UDELL",
    "UNION",
    "VERMILLION",
    "WALNUT",
    "WASHINGTON",
    "WELLS"
  };
}
