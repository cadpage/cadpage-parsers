package net.anei.cadpage.parsers.NM;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class NMSanJuanCountyAParser extends SmartAddressParser {

  public NMSanJuanCountyAParser() {
    super("SAN JUAN COUNTY", "NM");
    setFieldList("SRC ID DATE TIME CALL ADDR APT PLACE INFO");
  }

  private static Pattern MASTER = Pattern.compile("(\\d{2}-\\d+) (\\d{8}) (\\d{1,2}(?::\\d{2}){1,2}) (.*? [A-Z]) (.*?)", Pattern.DOTALL);
  private static Pattern YO = Pattern.compile("(?:Y/?O[MF]?\\b|\\d{4}\\(VERF\\)).*", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;
    data.strSource = subject;
    data.strCallId = mat.group(1);
    
    // Make DATE readable
    String date = mat.group(2);
    data.strDate = date.substring(0, 2) + "/" + date.substring(2, 4) + "/" + date.substring(4, 8);
    
    // TIME CALL ADDR APT
    data.strTime = mat.group(3);
    data.strCall = mat.group(4).trim();
    
    //Try standard parseAddress
    String group5 = mat.group(5).trim();
    Result res = parseAddress(StartType.START_ADDR, group5);
    if (res.isValid()) {
      parseResult(res, data);
      return true;
    }
    
    //Try parseAddress with optional street suffix
    res = parseAddress(StartType.START_ADDR, FLAG_OPT_STREET_SFX, group5);
    if (res.getStatus() >= STATUS_FULL_ADDRESS) {
      parseResult(res, data);
      return true;
    }
    
    //Pass whole field to info as last resort
    data.strSupp = group5;
    return true;
  }
  
  private static void parseResult(Result res, Data data) {
    res.getData(data);
    data.strPlace = data.strPlace.replace(": ", ":");
    int pt = data.strPlace.indexOf(" / ");
    if(pt >= 0) {
      data.strSupp = data.strPlace.substring(pt+3).trim();
      data.strPlace = data.strPlace.substring(0,pt).trim();
    }
    data.strSupp = append(data.strSupp, " / ", res.getLeft());
    Matcher yoMat = YO.matcher(data.strSupp);
    if (yoMat.matches()) {
      data.strSupp = append(data.strApt, " ", data.strSupp);
      data.strApt = "";
    }
  }

}
