package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSRiceCountyParser extends DispatchA25Parser {
 
  public KSRiceCountyParser() {
    super("RICE COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "enterpolalerts@rcsoks.org,general@ricecounty.us";  
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    
    if (data.strCity.equalsIgnoreCase("County")) data.strCity = "";
    
    Matcher match = BETWEEN_PTN.matcher(data.strAddress);
    if (match.matches()) {
      data.strAddress = match.group(1).trim();
      data.strCross = match.group(2).trim();
    }
    
    if (data.strPlace.length() > 0 && checkAddress(data.strAddress) == STATUS_NOTHING &&
        !CHK_ADDRESS_PTN.matcher(data.strAddress).matches()) {
      if (checkAddress(data.strPlace) > STATUS_NOTHING || CHK_ADDRESS_PTN.matcher(data.strPlace).matches()) {
        String tmp = data.strAddress;
        data.strAddress = "";
        parseAddress(data.strPlace, data);
        data.strPlace = tmp;
      }
    }
    return true;
  }
  private static final Pattern BETWEEN_PTN = Pattern.compile("(.*?) (?:& )?between (.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CHK_ADDRESS_PTN = Pattern.compile(".*&.*|\\d+ .*");
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("ADDR", "ADDR X");
  }
  
  @Override
  public String adjustMapAddress(String address) {
    address = DIR_BND_PTN.matcher(address).replaceAll("").trim();
    address = SECTOR_PTN.matcher(address).replaceAll("");
    address = FROM_PTN.matcher(address).replaceAll("&");
    return super.adjustMapAddress(address);
  }
  private static final Pattern DIR_BND_PTN = Pattern.compile("\\b[NSEW] *BND\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SECTOR_PTN = Pattern.compile("[- ]+[NSEW] SECTOR$", Pattern.CASE_INSENSITIVE);
  private static final Pattern FROM_PTN = Pattern.compile("\\bFROM\\b", Pattern.CASE_INSENSITIVE);
}
