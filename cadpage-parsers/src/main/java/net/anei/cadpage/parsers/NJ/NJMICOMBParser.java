package net.anei.cadpage.parsers.NJ;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
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
  protected boolean parseMsg(String subject, String body, Data data) {
    
    do {
      if (subject.equals("CAD Page")) break;
      
      if (subject.equals("MICCOM CAD")) break;
      if (subject.equals("MICCOM")) break;
      
      if (body.startsWith("/ CAD Page / ")) {
        body = body.substring(13);
        break;
      }
        
      return false;
    } while (false);

    FParser fp = new FParser(body);
    data.strUnit = fp.get(10);
    
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
      return true;
    }
    
    // Now check for regular dispatch page
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
      data.strTime = fp.get(5);
      return true;
    }
    
    // Another variate of the regular dispatch report.  This is probably a fixed position alert like the
    // others, but we loose the spacing when it is broken into two messages.  Fortunately the break always
    // occurs in the samem place.
    if (fp.check("RESP: ")) {
      setFieldList("UNIT CITY ADDR APT X CALL ID TIME");
      data.strCity = cleanCity(fp.get(15));
      if (!fp.check(" ")) return false;
      parseAddress(fp.get(30), data);
      if (!fp.check("BLDG:")) return false;
      data.strApt = append(data.strApt, "-", fp.get(5));
      if (!fp.check("APT:")) return false;
      body = fp.get();
      int pt = body.indexOf("Cross-");
      if (pt < 0) return false;
      data.strApt = append(data.strApt, "-", body.substring(0, pt).trim());
      fp = new FParser(body.substring(pt+6));
      data.strCross = fp.get(30);
      if (!fp.check(" ")) return false;
      data.strCall = fp.get(30);
      if (!fp.check("#")) return false;
      data.strCallId = fp.get(10);
      if (!fp.check(" @")) return false;
      data.strTime = fp.get(4);
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
  
  private static final Pattern CITY_SFX_PTN = Pattern.compile("( +(?:Boro|City))?(?: *\\([ A-Z]+\\))?$");
  
  private static String cleanCity(String city) {
    city = CITY_SFX_PTN.matcher(city).replaceFirst("");
    if (city.endsWith(" Tw")) city += 'p';
    return city;
  }
}
