package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class TNOakRidgeParser extends SmartAddressParser {

  private static final Pattern ORTP_PTN = Pattern.compile("\\bORTP\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern DELIM = Pattern.compile(" *(?://+|\\.\\.+|  +|--+| / ) *");
  private static final Pattern START_NUMBER_PTN = Pattern.compile("^\\d+[A-Z]? ");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|SUITE) *(.*)", Pattern.CASE_INSENSITIVE);
  
  private static final Pattern TENN_PTN = Pattern.compile("\\bTENN\\b", Pattern.CASE_INSENSITIVE);
  
  public TNOakRidgeParser() {
    super("OAK RIDGE", "TN");
    setFieldList("CALL ADDR PLACE APT INFO");
  }
  
  @Override
  public String getFilter() {
    return "@oakridgetn.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Body contains disclaimer and nothing else, the real information is in the subject
    if (!body.contains("Electronic communications with officials and employees of the City")) return false;
    
    // Expand ORTP -> OAK RIDGE TURNPIKE
    subject = ORTP_PTN.matcher(subject).replaceAll("OAK RIDGE TURNPIKE");
    
    // Break subject down into different fields
    String[] flds = DELIM.split(subject);
    
    // Lots of different cases, if there is only one field, use smart address parser to split it out
    if (flds.length == 1) {
      parseAddress(StartType.START_CALL, FLAG_NO_IMPLIED_APT, flds[0], data);
      String info = getLeft();
      if (data.strCall.length() == 0) data.strCall = getLeft();
      else if (data.strCall.length() + info.length() <= 30) {
        data.strCall = append(data.strCall, " - ", info);
      }
      else data.strSupp = info;
      data.strAddress = TENN_PTN.matcher(data.strAddress).replaceAll("TENNESSEE");
      return true;
    }
    
    int addrNdx = -1;
    Result bestRes = null;
    String bestPlace = null;
    for (int ndx = 0; ndx < flds.length; ndx++) {
      String fld = flds[ndx];
      if (fld.startsWith("/")) fld = fld.substring(1).trim();
      String place = null;
      int pt = fld.indexOf('/');
      if (pt >= 0) {
        place = fld.substring(pt+1).trim();
        fld = fld.substring(0,pt).trim();
      }
      Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, fld);
      if (!res.isValid()) res = parseAddress(StartType.START_ADDR, FLAG_NO_IMPLIED_APT, fld);
      if (res.isValid()) {
        bestRes = res;
        bestPlace = place;
        addrNdx = ndx;
        break;
      }
      else if (bestRes == null && (START_NUMBER_PTN.matcher(fld).find() || place != null)) {
        bestRes = res;
        bestPlace = place;
        addrNdx = ndx;
      }
    }
    
    if (addrNdx >= 0) {
      for (int ndx = 0; ndx < flds.length; ndx++) {
        String fld = flds[ndx];
        if (ndx == addrNdx) {
          bestRes.getData(data);
          if (bestPlace != null) {
            Matcher match = APT_PTN.matcher(bestPlace);
            if (match.matches()) {
              data.strApt = append(data.strApt, "-", match.group(1));
            } else {
              data.strPlace = bestPlace;
            }
          }
          fld = bestRes.getLeft();
          if (fld.length() == 0) continue;
        }
        if (ndx < addrNdx) {
          data.strCall = append(data.strCall, " - ", fld);
        } else {
          Matcher match = APT_PTN.matcher(fld);
          if (match.matches()) {
            data.strApt = append(data.strApt, "-", match.group(1));
          } else if (data.strSupp.length() == 0 && data.strCall.length() + fld.length() <= 30) {
            data.strCall = append(data.strCall, " - ", fld);
          } else {
            data.strSupp = append(data.strSupp, " / ", fld);
          }
        }
      }
      data.strAddress = TENN_PTN.matcher(data.strAddress).replaceAll("TENNESSEE");
      return true;
    }
    return false;
  }
}
