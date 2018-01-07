package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

/**
 * Chambers County, AL
 */
public class ALChambersCountyParser extends DispatchA65Parser {
  
  public ALChambersCountyParser() {
    super(CITY_LIST, "CHAMBERS COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBreakIns() { return true; }
      @Override public boolean splitKeepLeadBreak() { return true; }
    };
  }
  
  private static final Pattern ADDR_SECTOR_PTN = Pattern.compile("(.*) - [A-Z]+ SECTOR");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!super.parseMsg(subject, body, data)) return false;
    
    Matcher match = ADDR_SECTOR_PTN.matcher(data.strAddress);
    if (match.matches()) data.strAddress = match.group(1).trim();
    
    if (data.strCity.equalsIgnoreCase("WEST POINT")) data.strState = "GA";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static String[] CITY_LIST = new String[]{

      //INCORPORATED
      "CUSSETA",
      "FIVE POINTS",
      "LAFAYETTE",
      "LANETT",
      "VALLEY",
      "WAVERLY",
      
      //UNINCORPORATED
      "OAK BOWERY",
      "OAKLAND",
      "WHITE PLAINS",
      
      //CDPS
      "ABANDA",
      "FREDONIA",
      "HUGULEY",
      "PENTON",
      "STANDING ROCK",
      
      // Troup County, GA
      "WEST POINT"
  };
}
