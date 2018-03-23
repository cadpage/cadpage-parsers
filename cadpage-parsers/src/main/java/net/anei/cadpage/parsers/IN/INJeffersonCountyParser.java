package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;


public class INJeffersonCountyParser extends DispatchEmergitechParser {
  
  public INJeffersonCountyParser() {
    super(true, CITY_LIST, "JEFFERSON COUNTY", "IN", TrailAddrType.PLACE);
  }

  @Override
  public String getFilter() {
    return "@jeffersoncounty.in.gov";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
  
  private static final Pattern SUBJECT_GEN_ALERT_PTN = Pattern.compile("Text Message(?:|(\\S+))");
  private static final Pattern GEN_ALERT_TRIM_PTN = Pattern.compile("^[- ]+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match =  SUBJECT_GEN_ALERT_PTN.matcher(subject);
    if (match.matches()) {
      setFieldList("UNIT INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strUnit = getOptGroup(match.group(1));
      data.strSupp = GEN_ALERT_TRIM_PTN.matcher(body).replaceFirst("");
      return true;
    }
    
    if (subject.length() == 0) return false;
    body = subject + ": " + body;
    if (!body.contains("Nature:") && body.contains(" Location")) body = "Nature:" + body;
    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities and towns
    "BROOKSBURG",
    "DUPONT",
    "HANOVER",
    "MADISON",

    // Unincorporated towns
    "CANAAN",
    "KENT",
    "DEPUTY",

    // Townships
    "GRAHAM TWP",
    "HANOVER TWP",
    "LANCASTER TWP",
    "MADISON TWP",
    "MILTON TWP",
    "MONROE TWP",
    "REPUBLICAN TWP",
    "SALUDA TWP",
    "SHELBY TWP",
    "SMYRNA TWP"
  };
}
