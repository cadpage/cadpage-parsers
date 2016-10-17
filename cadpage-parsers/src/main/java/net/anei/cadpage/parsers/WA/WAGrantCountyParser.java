package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;

public class WAGrantCountyParser extends SmartAddressParser {

  public WAGrantCountyParser() {
    super("GRANT COUNTY", "WA");
    setAllowDirectionHwyNames();
  }

  private static Pattern MASTER2 = Pattern.compile("(?:([A-Za-z0-9]+):)?([^:]+?):([^:]+?):([^:]*?):INF:(.*?)(?: +(\\d\\d:\\d\\d:\\d\\d) +(\\d\\d/\\d\\d/\\d{4}))?(?: +- +([^:]+?))? *:CNT:(.*?):PH:(.*?)");
  private static Pattern PRI_CALL_PTN = Pattern.compile("([A-E]) +(.*)");
  private static Pattern UNIT_PRI_CALL_PTN = Pattern.compile("(.+?)[,;]? *\\b([A-E]) ([^,]+)");
  private static Pattern UNIT_CALL_PTN = Pattern.compile("(.*)[,;@](.*)");

  private static Pattern MASTER1 = Pattern.compile("(?:([^:]*?):([^:]*?): *P:([^:]*?)|([^:]*?)):([^:]*?):([^:]*?):? *:+INF:(.*?):CP:(.*?):(?:CNT:.{0,2}(\\d{2}:\\d{2}:\\d{2}) (\\d{2}/\\d{2}/\\d{2})(.*)|PH:(.*))");
  private static Pattern UNIT_PRI_CALL = Pattern.compile("(?:(.*?) +([A-E]) +(.*?)|(.*?,Sta ?\\d,?)([A-D]) *(.*?))");
  // Info field components
  private static Pattern PHONE = Pattern.compile("(.*?)(\\(\\d{3}\\)\\d{3}-\\d{4})\\b(.*?)");
  private static Pattern TIME = Pattern.compile("(.*?)\\b(\\d{2}:\\d{2}:\\d{2})\\b(.*?)");
  private static Pattern DATE = Pattern.compile("(.*?)\\b(\\d{2}/\\d{2}/\\d{2}\\d{2})\\b(?: - )?(.*?)");
  // For condensing consecutive spaces
  private static Pattern SPACE = Pattern.compile("  +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher mat = MASTER2.matcher(body);
    if (mat.matches()) {
      setFieldList("SRC PRI CALL ADDR APT PLACE CITY INFO TIME DATE UNIT NAME PHONE");
      data.strSource = getOptGroup(mat.group(1));
      String sourcePriCall = mat.group(2).trim();
      parseAddressField(mat.group(3).trim(), data);
      data.strCity = mat.group(4).trim();
      data.strSupp = mat.group(5).trim();
      data.strDate = getOptGroup(mat.group(6));
      data.strTime = getOptGroup(mat.group(7));
      data.strUnit = getOptGroup(mat.group(8));
      data.strName = mat.group(9).trim();
      data.strPhone =mat.group(10).trim();
      
      if (data.strSource.length() > 0) {
        mat = PRI_CALL_PTN.matcher(sourcePriCall);
        if (mat.matches()) {
          data.strPriority = mat.group(1);
          data.strCall = mat.group(2).trim();
        } else {
          data.strCall = sourcePriCall;
        }
      } else if ((mat = UNIT_PRI_CALL_PTN.matcher(sourcePriCall)).matches()) {
        data.strSource = mat.group(1).trim();
        data.strPriority = mat.group(2);
        data.strCall = mat.group(3).trim();
      } else if ((mat = UNIT_CALL_PTN.matcher(sourcePriCall)).matches()) {
        data.strSource = mat.group(1).trim();
        data.strCall = mat.group(2).trim();
      }
      else {
        String call = CALL_SET.getCode(sourcePriCall);
        if (call != null) {
          data.strCall = call;
          data.strSource = sourcePriCall.substring(0,sourcePriCall.length()-call.length()).trim();
        }
        else data.strCall = sourcePriCall;
      }
      return true;
    }
    
    mat = MASTER1.matcher(body);
    if (mat.matches()) {
      setFieldList("UNIT PRI CALL ADDR PLACE APT INFO PHONE TIME DATE NAME");
  
      // UNIT PRI CALL... three possible formats and last resort pass to strCall
      String g1 = mat.group(1);
      if (g1 != null) {
        data.strUnit = g1.trim();
        data.strCall = mat.group(2).trim();
        data.strPriority = mat.group(3).trim();
        if (data.strCall.length() >= 2 &&
            data.strCall.charAt(1) == ' ' &&
            data.strCall.startsWith(data.strPriority)) {
          data.strCall = data.strCall.substring(2).trim();
        }
      } else {
        String g4 = mat.group(4).trim();
        Matcher upcMat = UNIT_PRI_CALL.matcher(g4);
        if (upcMat.matches()) {
          if (upcMat.group(1) != null) {
            data.strUnit = upcMat.group(1).trim();
            data.strPriority = upcMat.group(2).trim();
            data.strCall = upcMat.group(3).trim();
          } else {
            data.strUnit = upcMat.group(4).trim();
            data.strPriority = upcMat.group(5).trim();
            data.strCall = upcMat.group(6).trim();
          }
        } else {
          String call = CALL_SET.getCode(g4);
          if (call != null) {
            data.strCall = call;
            data.strSource = g4.substring(0,g4.length()-call.length()).trim();
          }
          else data.strCall = g4;
        }
      }
  
      // ADDR APT PLACE
      parseAddressField(mat.group(5).trim(), data);
      data.strPlace = append(data.strPlace, "; ", mat.group(6).trim());
  
      // Parse phone date n time out of INF:
      String info = mat.group(7).trim();
      Matcher iMat = PHONE.matcher(info);
      if (iMat.matches()) {
        data.strPhone = iMat.group(2).trim();
        info = append(iMat.group(1).trim(), " ", iMat.group(3).trim());
      }
      iMat = TIME.matcher(info);
      if (iMat.matches()) {
        data.strTime = iMat.group(2).trim();
        info = append(iMat.group(1).trim(), " ", iMat.group(3).trim());
      }
      iMat = DATE.matcher(info);
      if (iMat.matches()) {
        data.strDate = iMat.group(2).trim();
        info = append(iMat.group(1).trim(), " ", iMat.group(3).trim());
      }
      data.strSupp = info;
  
      // Append CP:(.*?) to unit with repeating spaces condensed
      data.strUnit = append(data.strUnit, " ", SPACE.matcher(mat.group(8)).replaceAll(" ").trim());
  
      // if CNT:(.*?) exists parse data that hasn't already been taken
      String g9 = mat.group(9);
      if (g9 != null) {
        if (data.strTime.length() == 0) data.strTime = g9.trim();
        if (data.strDate.length() == 0) data.strDate = mat.group(10).trim();
        data.strName = mat.group(11).trim();
      }
      return true;
    }
    
    return false;
  }

  private void parseAddressField(String addr, Data data) {
    Matcher aMat = ADDR.matcher(addr);
    if (aMat.matches()) {
      addr = aMat.group(1);
      String g2 = aMat.group(2);
      if (g2 != null) data.strApt = g2;
      else data.strPlace = aMat.group(3);
    }
    parseAddress(addr, data);
  }
  private static Pattern ADDR = Pattern.compile("(.*?); *(?:(?:#|APT|ROOM|RM|STE) *([A-Z]*\\d+)|(.*?))", Pattern.CASE_INSENSITIVE);
  
  @Override
  public CodeSet getCallList() {
    return CALL_SET;
  }
  
  private static final ReverseCodeSet CALL_SET = new ReverseCodeSet(
      "911 Hang Up",
      "A Smoke Invest",
      "Abdominal",
      "Alarms - Fire",
      "Back Pain",
      "Breathing",
      "Breathing Prob",
      "Cardiac Arst",
      "Chest Pain",
      "Citizen Asst",
      "Convulsions",
      "Diabetic Prob",
      "Electric Haz",
      "Falls",
      "Heart Problem",
      "Hemorrhage",
      "Outside Fire",
      "Overdose/Pois",
      "Public Service",
      "Public Srvc",
      "Sick Person",
      "Smoke Invest",
      "Stroke",
      "Structure Fir",
      "Suicide Thrt",
      "Traffic Acc",                                                                                                                                                                                                 
      "Transfer",
      "Traumatic Inj",
      "Unconscious",
      "Unknown Prob",
      "Vehicle Fire"
  );

}
