package net.anei.cadpage.parsers.IN;



import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA56Parser;

/**
 * Parke County, IN
 */
public class INParkeCountyParser extends DispatchA56Parser {
  
  public INParkeCountyParser() {
    super("PARKE COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@bloomingdaletel.com,DISPATCH@parkecounty-in.gov,parkecountydispatch911@gmail.com,DISPATCHtext@parkecounty-in.gov,ParkeCountyDispatch@outlook.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("DISPATCH:")) body = "DISPATCH:" + body;
    return super.parseMsg(body, data);
  }
   
}
