package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class ALHooverParser extends SmartAddressParser {

  public ALHooverParser() {
    super("HOOVER", "AL");
    setFieldList("UNIT ADDR APT TIME DATE CALL INFO PLACE ID X");
  }

  private static Pattern MASTER = Pattern.compile("\'(?:Unit:(.*?))? *Loc:(.*?) *Time:(.*?) *Date:(.*?) *Inc:(.*?) *Addtl:(.*?) *(?:UnitSts:.*?)? *Venue:(?:HOOVER *)?(.*?) *Inc#:(.*?) *Between:(.*?)\'.*?");
  private static Pattern SUBJECT = Pattern.compile("Robot ALERT ID-(\\d{9}) ACTIVE911  [A-Z0-9]+");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //id from subject
    Matcher sMat = SUBJECT.matcher(subject);
    if (!sMat.matches()) return false;
    data.strCallId = sMat.group(1);
    
    //fields from body
    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;
    data.strUnit = getOptGroup(mat.group(1));
    parseAddress(mat.group(2), data);
    data.strTime = mat.group(3);
    data.strDate = mat.group(4);
    data.strCall = mat.group(5);
    data.strSupp = mat.group(6);
    data.strPlace = mat.group(7);
    data.strCallId = append(data.strCallId, " / ", mat.group(8));
    data.strCross = mat.group(9);
    return true;
  }

}
