package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class DispatchA46Parser extends SmartAddressParser {
  
  String defState;
  Properties callCodes;

  public DispatchA46Parser(String defState, String defCity) {
    this(null, defState, defCity);
  }

  public DispatchA46Parser(Properties callCodes, String defState, String defCity) {
    super(defState, defCity);
    this.defState = defState;
    this.callCodes = callCodes;
  }

  private static Pattern SUBJECT_PTN1 = Pattern.compile("([A-Z0-9]{3,6}) +- +(.*?) +- +(\\d{10})\\b[- ]*(.*)");
  private static Pattern BODY_PTN1 = Pattern.compile("(?:There has been a\\(n\\) +)?(.*?) +reported (at|across from) +(.*)");
  private static Pattern ADDR_PTN1 = Pattern.compile("([^,]*),([^,]*), *([A-Z]{2})\\b,? *(.*)");
  
  private static Pattern SUBJECT_PTN2 = Pattern.compile("([A-Z0-9]{3,6}) *- +(?:.*\\|)?(.*?)");
  private static Pattern ID_PTN = Pattern.compile("\\d{10}");
  private static Pattern BODY_PTN2 = Pattern.compile("(?:A(?:\\(n\\))? *)?(.*?) has been reported at (.*?)");
  private static Pattern ADDR_PTN2A = Pattern.compile("([^,]*),(?:([^,]*),)? *([A-Z]{2})\\.?(?:[ ,]+(20\\d{8})?(?:,? *(.*))?)?");
  private static Pattern ADDR_PTN2B = Pattern.compile("(.*?),(?:([^,]*),)? *([A-Z]{2})\\.?(?:[ ,]+(20\\d{8})?(?:,? *(.*))?)?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // We handle two page formats, believed to be two versions of the same CAD system
    Matcher mat = SUBJECT_PTN1.matcher(subject);
    if (mat.matches()) {
      setFieldList("SRC CODE ID CALL PLACE ADDR APT CITY ST INFO");
      
      data.strSource = getOptGroup(mat.group(1));
      data.strCall = mat.group(2);
      data.strCallId = mat.group(3);
      String subAddr = mat.group(4);
      
      int pt = body.indexOf('\n');
      if (pt >= 0) {
        data.strSupp = body.substring(pt+1).trim().replaceAll("\n\n+", "\n");
        body = body.substring(0,pt).trim();
      }
  
      // parse any unfound info from body and save body's trail
      mat = BODY_PTN1.matcher(body);
      String addr;
      if (mat.matches()) {
        String call = stripFieldEnd(mat.group(1), "-");;
        if (!call.equals(data.strCall)) {
          data.strCode = data.strCall;
          data.strCall = call;
        }
        if (callCodes != null) {
          call = callCodes.getProperty(data.strCall);
          if (call != null) {
            data.strCode = data.strCall;
            data.strCall = call;
          }
        }
        String place = mat.group(2);
        if (!place.equals("at")) data.strPlace = place;
        addr = mat.group(3).trim();
      } else {
        if (subAddr.length() == 0) return false;
        data.strSupp = append(body, "\n", data.strSupp);
        addr = subAddr;
      }
      addr = addr.replace('@', '&');
      mat = ADDR_PTN1.matcher(addr);
      if (mat.matches()) {
        parseAddress(mat.group(1).trim(), data);
        data.strCity = mat.group(2).trim();
        data.strState = mat.group(3);
        data.strSupp = append(mat.group(4), "\n", data.strSupp);
      } else {
        pt = addr.indexOf(',');
        if (pt >= 0) {
          data.strSupp = addr.substring(pt+1).trim();
          addr = addr.substring(0,pt);
          parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, addr, data);
        } else {
          parseAddress(StartType.START_ADDR, addr, data);
          data.strSupp = getLeft();
        }
      }
      return true;
    }
    
    mat = SUBJECT_PTN2.matcher(subject);
    if (mat.matches()) {
      setFieldList("SRC CODE CALL ADDR APT CITY ST ID INFO");
      
      data.strSource = getOptGroup(mat.group(1));
      String code = mat.group(2);
      if (ID_PTN.matcher(code).matches()) {
        data.strCallId = code;
      } else {
        data.strCode = code;
      }

      StartType st;
      int flags;
      String addr;
      mat = BODY_PTN2.matcher(body);
      if (mat.matches()) {
        st = StartType.START_ADDR;
        flags = 0;
        data.strCall = mat.group(1).trim();
        addr = mat.group(2).trim();
      } else {
        st = StartType.START_CALL;
        flags = FLAG_START_FLD_REQ;
        addr = body;
      }

      Pattern ptn = (st == StartType.START_ADDR ? ADDR_PTN2A : ADDR_PTN2B);
      mat = ptn.matcher(addr);
      if (mat.matches()) {
        addr = mat.group(1).trim().replace('@', '&');
        parseAddress(st, flags | FLAG_ANCHOR_END, addr, data);
        data.strCity = getOptGroup(mat.group(2));
        data.strState = mat.group(3);
        if (data.strCallId.length() == 0) data.strCallId = getOptGroup(mat.group(4));
        data.strSupp = getOptGroup(mat.group(5)).replaceAll("  +", " ");
      } else {
        if (st == StartType.START_CALL) return false;
        parseAddress(st, flags, addr, data);
        String left = getLeft();
        left = stripFieldStart(left, "-");
        data.strSupp = left;
      }

      if (callCodes != null) {
        String call = callCodes.getProperty(data.strCall);
        if (call != null) {
          data.strCode = data.strCall;
          data.strCall = call;
        }
      }
      return true;
    }
    
    return false;
  }
}
