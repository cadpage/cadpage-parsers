package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class DispatchA46Parser extends SmartAddressParser {

  private Properties callCodes;
  private boolean noCities;

  public DispatchA46Parser(String defState, String defCity) {
    this(null, null, defState, defCity);
  }

  public DispatchA46Parser(Properties callCodes, String defState, String defCity) {
    this(callCodes, null, defState, defCity);
  }

  public DispatchA46Parser(String[] cityList, String defState, String defCity) {
    this(null, cityList, defState, defCity);
  }

  public DispatchA46Parser(Properties callCodes, String[] cityList, String defState, String defCity) {
    super(cityList, defState, defCity);
    this.callCodes = callCodes;
    noCities = cityList == null;
  }

  private static final Pattern SUBJECT_PTN1 = Pattern.compile("([A-Z0-9]{3,6}) +- +(.*?) +- +(\\d{10})\\b[- ]*(.*)");
  private static final Pattern BODY_PTN1 = Pattern.compile("(?:There has been a\\(n\\) +)?(.*?) +reported (at|across from) +(.*)");
  private static final Pattern ADDR_PTN1 = Pattern.compile("([^,]*),([^,]*), *([A-Z]{2})\\b,? *(.*)");

  private static final Pattern SUBJECT_PTN2 = Pattern.compile("([A-Z0-9]{3,6}) *- +(?:.*\\|)?(.*?)");
  private static final Pattern ID_PTN = Pattern.compile("\\d{10}");
  private static final Pattern BODY_PTN2 = Pattern.compile("(?:A\\b(?:\\(n\\))? *)?(.*?) has been reported at (.*?)");
  private static final Pattern ADDR_PTN2A = Pattern.compile("([^,]*),(?:([^,]*),)? *([A-Z]{2})\\.?(?:[ ,]+(20\\d{8})?(?:,? *(.*))?)?");
  private static final Pattern ADDR_PTN2B = Pattern.compile("(.*?),(?:([^,]*),)? *([A-Z]{2})\\.?(?:[ ,]+(20\\d{8})?(?:,? *(.*))?)?");
  private static final Pattern ADDR_PTN3 = Pattern.compile("(.*?)[, ]+#?(\\d{2}-\\d+)(\\*.*)");
  private static final Pattern INFO_HEAD_PTN = Pattern.compile(".*?\\b\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?: *(.*)");

  private static final Pattern SUBJECT_PTN3 = Pattern.compile("CAD (?:Autopage|Page for) EventID: *(\\d+)");
  private static final Pattern BODY_PTN3 =  Pattern.compile("(?:A(?:\\(n\\))? )?(.*?) has been reported at (.*?) on (\\d\\d?/\\d\\d?/\\d{4}) at (\\d\\d?:\\d\\d [AP]M)\\. *(.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");

  private static final Pattern BODY_PTN4 = Pattern.compile("(?:There has been a|A)\\(n\\) (.*?) (?:has been )?reported(.*?) at (.*?)");
  private static final Pattern TRAIL_DOT_PTN = Pattern.compile(" +0*\\.$");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {


    // We handle three page formats, believed to be two versions of the same CAD system
    // But both of them can be followed by new line separated information
    body = body.replace("\n has been reported", " has been reported");
    String info = "";
    int pt = body.indexOf('\n');
    if (pt >= 0) {
      info = body.substring(pt+1).trim();
      body = body.substring(0,pt).trim();
    }

    body = stripFieldStart(body, "**UPDATED ADDRESS**");

    Matcher mat = SUBJECT_PTN1.matcher(subject);
    if (mat.matches()) {
      setFieldList("SRC CODE ID CALL PLACE ADDR APT CITY ST INFO");

      data.strSource = getOptGroup(mat.group(1));
      data.strCall = mat.group(2);
      data.strCallId = mat.group(3);
      String subAddr = mat.group(4);

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
    }

    else if ((mat = SUBJECT_PTN2.matcher(subject)).matches()) {
      setFieldList("SRC CODE CALL ADDR PLACE? X? APT CITY ST ID INFO");

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
      } else if ((mat = ADDR_PTN3.matcher(addr)).matches()) {
        addr = mat.group(1).trim().replace('@',  '&');
        parseAddress(st, flags | FLAG_ANCHOR_END, addr, data);
        data.strCallId = mat.group(2);
        data.strSupp = mat.group(3);

      } else {
        if (st == StartType.START_CALL) return false;
        addr = addr.replace('@', '&');
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
    }

    else {
      if ((mat = SUBJECT_PTN3.matcher(subject)).matches()) data.strCallId = mat.group(1);
      body = stripFieldStart(body, "A ");
      mat = BODY_PTN3.matcher(body);
      if (mat.matches()) {
        setFieldList("ID CALL ADDR APT CITY ST DATE TIME INFO");
        data.strCall = mat.group(1);
        String addr = mat.group(2);
        data.strDate =  mat.group(3);
        setTime(TIME_FMT, mat.group(4), data);
        data.strSupp = mat.group(5);

        parseThisAddress(addr, data);
      }

      else if ((mat = BODY_PTN4.matcher(body)).matches()) {
        setFieldList("ID CALL ADDR APT CITY ST INFO");
        data.strCall = append(mat.group(1).trim(), " ", mat.group(2).trim());
        body = mat.group(3).trim();
        body = TRAIL_DOT_PTN.matcher(body).replaceFirst("");
        body = stripFieldEnd(body,  ".");
        parseThisAddress(body, data);
      }

      else return false;
    }

    for (String line : info.split("\n+")) {
      line = line.trim();
      Matcher match = INFO_HEAD_PTN.matcher(line);
      if (match.matches()) line = match.group(1);
      data.strSupp = append(data.strSupp, "\n", line);
    }
    return true;
  }

  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*?), ([A-Z]{2})(?: (\\d{5}|0000)(?:-\\d+)?)? *(.*)");
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*?) (\\d{5}|0000)(?:-\\d+)?");

  private void parseThisAddress(String addr, Data data) {
    int pt;
    Matcher mat;
    String zip = null;
    String info = null;
    mat = ADDR_ST_ZIP_PTN.matcher(addr);
    if (mat.matches()) {
      addr = mat.group(1).trim();
      data.strState = mat.group(2);
      zip = mat.group(3);
      info = mat.group(4);

      pt = addr.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = addr.substring(pt+1).trim();
        parseAddress(addr.substring(0,pt).trim(), data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
      }
    } else {
      mat = ADDR_ZIP_PTN.matcher(addr);
      if (mat.matches()) {
        addr = mat.group(1).trim();
        zip = mat.group(2);
      }
      pt = addr.indexOf(',');
      if (pt >= 0) {
        parseAddress(addr.substring(0,pt).trim(), data);
        String city = addr.substring(pt+1).trim();
        if (noCities) {
          data.strCity = city;
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
          info = getLeft();
        }
      }
      else {
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
      }
    }

    if (data.strCity.length() == 0 && zip != null && !zip.equals("0000")) data.strCity = zip;
    if (info != null) data.strSupp = append(data.strSupp, " ", info);
  }
}
