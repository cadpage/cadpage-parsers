package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;



public class NYOrangeCountyBParser extends DispatchPrintrakParser {
  
  private static final Properties CITY_TABLE = buildCodeTable(new String[]{
  });
  
  public NYOrangeCountyBParser() {
    super(CITY_TABLE, "ORANGE COUNTY", "NY");
  }
  
  @Override
  public String getFilter() {
    return "OC911@,messaging@iamresponding.com,@oc911.smartmsg.com,@mail.oc911.smartmsg.com,noreply@rover.spotteddogtech.com,@rovernoreply.spotteddogtech.com,@response.spotteddogtech.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(?:(\\d{8}-\\d{6}) +)?(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d)[-_ ]?");
  private static final Pattern TRAIL_ID_PTN = Pattern.compile(" +CASE: *([^ ]+)$");
  private static final Pattern TRAILER_PTN = Pattern.compile("(?:(?:Case #([^ ]{14})      <>|>>(.{0,25}))(?:(\\d{2})(\\d{6}) *,(\\d{2})(\\d{6})|0 +,0|))(?: +VEH: *([A-Za-z0-9 ]+))?$");
  private static final Pattern MISSING_TYP_PTN = Pattern.compile("[A-Za-z ]+ (?:EMS|FD) (?=[-/ A-Za-z]+ AD:)");
  private static final Pattern UNIT_MAP_BRK_PTN = Pattern.compile(" {2,}|(?<= EMS| FD) ");
  private static final Pattern INFO_SPLIT_PTN = Pattern.compile("[, ]*\\[\\d\\] *");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\[ProQA[^\\]]*|Automatic Case Number\\(s\\) issued.*|COS:.*|Dispatch Level:");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = cleanExtendedChars(body);
    
    String trailCall = "";
    Matcher match = DATE_TIME_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCallId = getOptGroup(match.group(1));
      data.strDate = match.group(2);
      data.strTime = match.group(3);
      body = body.substring(match.end());
      
      match = MISSING_TYP_PTN.matcher(body);
      if (match.lookingAt()) {
        body = match.group() + "TYP:" + body.substring(match.end());
      }
      
      match = TRAIL_ID_PTN.matcher(body);
      if (match.find()) {
        data.strCallId = append(data.strCallId, " / ", match.group(1));
        body = body.substring(0,match.start());
      }
    
      match = TRAILER_PTN.matcher(body);
      if (match.find()) {
        body = body.substring(0,match.start()).trim();
        String id = match.group(1);
        if (id != null) {
          data.strCallId = id;
        } else {
          trailCall = match.group(2).trim();
        }
        String gps1 = match.group(3);
        if (gps1 != null) {
          setGPSLoc(gps1+'.'+match.group(4)+','+match.group(5)+'.'+match.group(6), data);
        }
        data.strUnit = getOptGroup(match.group(7));
      }
    }
    
    if (body.startsWith("Village of Goshen PD          ")) {
       body = body.substring(0,30) + "TYP:" + body.substring(30);
       body = body.replace(" ADDRESS:", " AD:").replace(" CALLER:", " CN:");
    }
    
    if (!super.parseMsg(body, data)) return false;
    
    match = UNIT_MAP_BRK_PTN.matcher(data.strUnit);
    if (match.find()) {
      data.strMap = data.strUnit.substring(match.end());
      data.strUnit = data.strUnit.substring(0, match.start());
    }
    
    // Special handling for Ulster county calls
    if (data.strCity.length() == 0 && data.strName.equals("ULSTER 911")) {
      data.strCity = "ULSTER COUNTY";
    }
    
    data.strCall = append(data.strCall, " - ", trailCall);
    
    data.strCity = stripFieldEnd(data.strCity, " V");

    String info = "";
    for (String part:INFO_SPLIT_PTN.split(data.strSupp)) {
      part = part.trim();
      if (INFO_JUNK_PTN.matcher(part).matches()) continue;
      if ("[ProQA".startsWith(part)) continue;
      part = part.replace("[ProQA Dispatch]", "").replace("[ProQA: Case Entry Complete]", "")
                 .replace("[ProQA: Key Questions]", "")
                 .replace("[ProQA Session Aborted]", "").replace("[Shared]", "").trim();
      info = append(info, "\n", part);
    }
    data.strSupp = info;
    return true;
  }
  
  @Override
  public String getProgram() {
    String result = super.getProgram().replace("NAME", "CITY NAME") + " ID GPS UNIT MAP";
    if (result.indexOf("TIME") < 0) result = "DATE TIME " + result;
    return result;
  }
}
