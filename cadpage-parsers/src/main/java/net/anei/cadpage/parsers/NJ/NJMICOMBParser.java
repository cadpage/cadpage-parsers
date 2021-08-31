package net.anei.cadpage.parsers.NJ;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class NJMICOMBParser extends MsgParser {
  
  public NJMICOMBParser() {
    super("", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "miccom@nnjems.org,cadsmtp@nnjems.org";
  }
  
  @Override
  public String getLocName() {
    return "MICCOM (northern NJ), NJ";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitBreakLength() { return 160; }
      @Override public int splitBreakPad() { return 2; }
    };
  }
  
  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d:\\d\\d|");
  private static final Pattern SPACE_COMMA_PTN = Pattern.compile(" *, *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    do {
      if (subject.equals("CAD Page")) break;
      
      if (subject.equals("MICCOM CAD")) break;
      if (subject.equals("MICCOM")) break;
      
      if (body.startsWith("/ CAD Page / ")) {
        body = body.substring(13).trim();
        break;
      }
      
      if (body.startsWith("CAD Page / ")) {
        body = body.substring(11).trim();
        break;
      }
      
      if (body.startsWith("MICCOM / ")) {
        body = body.substring(9).trim();
        body = body.replace("MICCOM / ", " ");
        break;
      }
        
      return false;
    } while (false);
    
    // Fix problems with fixed length message split across two messages.
    if (substring(body, 10, 16).equals("RESP:")) {
      if (body.length() <= 160) {
        data.expectMore = true;
      } else {
        int pt2 = body.indexOf("Cross-");
        if (pt2 < 10) return false;
        if (pt2 < 138) {
          StringBuffer sb = new StringBuffer(body.substring(0, pt2));
          while (sb.length() < 138) sb.append(' ');
          sb.append(body.substring(pt2));
          body = sb.toString();
        }
        
        else {
          if (body.length() >= 160) {
            char chr1 = body.charAt(159);
            char chr2 = body.charAt(160);
            if (chr1 >= 'a' && chr1 <= 'z' && chr2 >= 'A' && chr2 <= 'Z') {
              body = body.substring(0,160) + ' ' + body.substring(160);
            }
          }
          
          int pt3 = body.indexOf(" #", 137);
          if (pt3 >= 0) {
            pt3 -= 29;
            if (pt3 < 175) {
              StringBuffer sb = new StringBuffer(body.substring(0, pt3));
              while (sb.length() < 175) sb.append(' ');
              sb.append(body.substring(pt3));
              body = sb.toString();
            }
          }
        }
      }
    }

    FParser fp = new FParser(body);
    data.strUnit = fp.get(10);
    
    // New run report format
    if (fp.check("@")) {
      setFieldList("UNIT ID PLACE ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strPlace = fp.getOptional("#", 21, 31);
      if (data.strPlace == null) return false;
      data.strCallId = fp.get(10);
      if (!fp.check(" ")) return false;
      data.strSupp = fp.get();
      return true;
    }
    
    // There are two flavors of run report, once for cancelled calls and one
    // for normal termination calls
    if (fp.checkAhead(32, "#") && fp.checkAhead(43, " Disp")) {
      setFieldList("UNIT PLACE ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strPlace = stripFieldStart(fp.get(32), "@");
      if (!fp.check("#")) return false;
      data.strCallId = fp.get(10);
      if (!fp.check(" ")) return false;
      data.strSupp = fp.get();
      return true;
    }
    
    if (fp.check("CANCEL: #")) {
      setFieldList("UNIT CALL ID CITY ADDR APT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCall = "CANCEL";
      data.strCallId = fp.get(10);
      if (!fp.check(" ")) return false;
      data.strCity = cleanCity(fp.get(20));
      parseAddress(fp.get(30), data);
      data.strSupp = fp.get();
      if (data.strSupp.endsWith("Cxl Rsn:")) data.expectMore = true;
      return true;
    }
    
    // Check for new regular dispatch page
    if (fp.check("RESP: ")) {
      setFieldList("UNIT CITY ADDR APT PLACE X CALL ID TIME NAME");
      data.strCity = cleanCity(fp.get(15));
      if (data.strCity.endsWith(" T")) data.strCity += "wp";
      if (!fp.check(" ")) return false;
      if (fp.check(" ")) return false;
      parseAddress(fp.get(30), data);
      if (!fp.check("BLDG:")) return false;
      String apt = fp.getOptional("APT:", 4, 5);
      if (apt == null) return false;
      data.strApt = append(data.strApt, "-", apt);
      data.strApt = append(data.strApt, "-", fp.get(10));
      if (!fp.check(" ")) return false;
      fp.setOptional();
      String place = fp.getOptional(" Cross-", 40, 50);
      if (place == null) place = fp.get();
      data.strPlace = place;
      data.strCross = fp.get(30);
      if (!fp.check(" ")) return false;
      data.strCall = fp.get(30);
      fp.check(" ");
      if (!fp.check("#")) return false;
      data.strCallId = fp.get(10);
      if (!fp.check(" @")) return false;
      String time = fp.get(5);
      if (!TIME_PTN.matcher(time).matches()) return false;
      saveTime(time, data);
      data.strName = SPACE_COMMA_PTN.matcher(fp.get()).replaceAll(", ");
      return true;
    }
    
    // Now check for old regular dispatch page
    if (fp.check("RESPOND:#")) {
      setFieldList("UNIT ID CITY ADDR APT X CALL TIME");
      data.strCallId = fp.get(10);
      if (!fp.check(" ")) return false;
      data.strCity = cleanCity(fp.get(20));
      parseAddress(fp.get(30), data);
      if (fp.checkAhead(71, "@") && !fp.check(" ")) return false;
      data.strApt = fp.get(10);
      data.strCross = fp.get(30);
      data.strCall = fp.get(30);
      if (!fp.check("@ ")) return false;
      String time = fp.get(5);
      if (!TIME_PTN.matcher(time).matches()) return false;
      saveTime(time, data);
      return true;
    }
    
    // Pt transfers have a different format
    if (fp.check("#")) {
      setFieldList("UNIT ID CALL CITY ADDR APT NAME INFO");
      data.strCallId = fp.get(10);
      if (!fp.check(" ")) return false;
      data.strCall = fp.get(30);
      data.strCity = cleanCity(fp.get(20));
      parseAddress(fp.get(30), data);
      data.strApt = fp.get(10);
      if (!fp.check("Patient:")) return false;
      data.strName = fp.get(36).replaceAll(" +,", ", ");
      data.strSupp = fp.get();
      return true;
    }
    
    if (substring(body, 10, 11).equals("#") && 
        substring(body, 112, 120).equals("Patient:")) {
      setFieldList("UNIT ID CALL CITY ADDR APT NAME INFO");
      data.strCallId = substring(body, 11, 21);
      data.strCall = substring(body, 22, 52);
      data.strCity = cleanCity(substring(body, 52, 72));
      parseAddress(substring(body, 72, 102), data);
      data.strApt = substring(body, 102, 112);
      data.strName = substring(body, 120, 156).replaceAll(" +,", ", ");
      data.strSupp = substring(body, 156);
      return true;
    }
    
    return false;
  }
  
  private static final Pattern CITY_SFX_PTN = Pattern.compile("( +(?:Boro?|City|Villa))?(?: *\\([ A-Z]+\\))?$");
  
  private static String cleanCity(String city) {
    city = CITY_SFX_PTN.matcher(city).replaceFirst("");
    if (city.endsWith(" Tw")) city += 'p';
    if (city.endsWith(" Town")) city += "ship";
    return city;
  }
  
  private static void saveTime(String time, Data data) {
    
    // Save time field.  For interfacility transfers, the is the requested transfer time
    // not the actual dispatch time.
    
    if (time.length() == 0) return;
    if (data.strCall.equals("Trans/Interfacility/Palliative")) {
      data.strCall = data.strCall + " @" + time;
    } else {
      data.strTime = time;
    }
  }
}
