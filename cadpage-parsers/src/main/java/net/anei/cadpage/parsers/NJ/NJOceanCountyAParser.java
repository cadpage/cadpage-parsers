package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;


/**
 * Ocean County, NJ
 */
public class NJOceanCountyAParser extends DispatchA9Parser {
  
  private static final Pattern CITY_PTN = Pattern.compile("(.*?) +(?:BORO|- NON SPECIFI?C?)", Pattern.CASE_INSENSITIVE);
  
  public NJOceanCountyAParser() {
    super(null, "OCEAN COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "Fire_Records,FireRecords,EMS_Records,EMS_Info,Police_Records";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Correct double spacing by iamresponding
    if (body.startsWith("Rip and Run Report\n\n\n\n~")) body = body.replace("\n\n", "\n");
    
    body = body.replace("JACOBSTOWN - NEW EGYPT", "JACOBSTOWN-NEW EGYPT");
    
    if (!super.parseMsg(body, data)) return false;
    
    // Dispatch seems to be storing the station code in the map fields
    data.strSource = data.strMap;
    data.strMap = "";
    
    // Have to clean up some city fields
    Matcher match = CITY_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);
    data.strCity = stripFieldEnd(data.strCity, ".");
    if (data.strCity.endsWith(" Co")) data.strCity += "unty";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
