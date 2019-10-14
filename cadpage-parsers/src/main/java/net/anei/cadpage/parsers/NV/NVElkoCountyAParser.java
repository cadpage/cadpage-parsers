package net.anei.cadpage.parsers.NV;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


/**
 * Elko County, NV
 */
public class NVElkoCountyAParser extends DispatchA9Parser {
  
  private static final Pattern I_ADDR_PTN = Pattern.compile("I[- ]?\\d+(?: +[NSEW]B)?");
    
  public NVElkoCountyAParser() {
    super(null,"ELKO COUNTY", "NV");
    setupProtectedNames("P X RANCH");
  }
  
  @Override
  public String getFilter() {
    return "ElkoDispatch@ci.elko.nv.us,ElkoDispatch@elkocitynv.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // Fix interstate mile markers
    if (data.strApt.length() > 0 && I_ADDR_PTN.matcher(data.strAddress).matches()) {
      data.strAddress = data.strAddress + ' ' + data.strApt;
      data.strApt = "";
    }


    // Fix city problems
    if (data.strCity.toUpperCase().startsWith("SPRING CREEK ")) {
      data.strCity = data.strCity.substring(0,12);
    }
    else if (data.strCity.equalsIgnoreCase("COUNTY")) data.strCity = "";
    return true;
  }
  
}
