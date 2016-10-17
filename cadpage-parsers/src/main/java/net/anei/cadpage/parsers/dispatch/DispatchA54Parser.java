package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA54Parser extends SmartAddressParser {
  
  // What should we do with trailing information
  protected enum DataType {INFO, NAME};
  
  private Pattern phonePtn;
  private static final Pattern APT_PTN = Pattern.compile("^Apt: *([A-Z0-9]+) +Bldg\\b");
  
  
  private DataType dataType;
  
  public DispatchA54Parser(String defCity, String defState, DataType dataType, String nullPhone) {
    super(defCity, defState);
    setFieldList("CALL ADDR PHONE APT X NAME INFO");
    this.dataType = dataType;
    
    phonePtn = Pattern.compile("\\b(?:(\\d{10})|" + nullPhone + ")\\b");
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("911-CENTER:")) return false;
    body = body.substring(11).trim();
    String sInfo;
    Matcher match = phonePtn.matcher(body);
    if (match.find()) {
      String sAddr = body.substring(0,match.start()).trim();
      sAddr = sAddr.replace('@', '&');
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, sAddr, data);
      data.strPhone = getOptGroup(match.group(1));
      sInfo = body.substring(match.end()).trim();
    } else {
      body = body.replace('@',  '&');
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_CROSS_FOLLOWS, body, data);
      sInfo = getLeft();
    }
    
    match = APT_PTN.matcher(sInfo);
    if (match.find()) {
      data.strApt = append(data.strApt, "-", match.group(1));
      sInfo = sInfo.substring(match.end()).trim();
      String bldg = null;
      if (sInfo.length() <= 3) {
        bldg = sInfo;
        sInfo = "";
      } else {
        int pt = sInfo.indexOf(' ');
        if (pt == 1) {
          char chr = sInfo.charAt(0);
          if ("NSEW".indexOf(chr) < 0) {
            bldg = "" + chr;
            sInfo = sInfo.substring(2).trim();
          }
        }
      }
      if (bldg != null) data.strApt = data.strApt + " Bldg " + bldg;
    }
    
    Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, sInfo);
    if (res.isValid()) {
      res.getData(data);
      sInfo = res.getLeft();
    }
    
    switch (dataType) {
    case INFO:
      data.strSupp = sInfo;
      break;
      
    case NAME:
      data.strName = cleanWirelessCarrier(sInfo);
      break;
    }
    
    return true;
  }

}
