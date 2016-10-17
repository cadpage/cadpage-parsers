package net.anei.cadpage.parsers.MN;


import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;

public class MNWatonwanCountyParser extends DispatchA38Parser {
 
  public MNWatonwanCountyParser() {
    super("WATONWAN COUNTY", "MN");
  }

  @Override
  public String getFilter() {
    return "NoReplyTAC10@co.watonwan.mn.us";
  }

}
