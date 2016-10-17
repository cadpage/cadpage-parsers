package net.anei.cadpage.parsers.OK;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class OKCherokeeCountyParser extends SmartAddressParser {

  public OKCherokeeCountyParser() {
    super(CITY_LIST, "CHEROKEE COUNTY", "OK");
    setFieldList("ADDR CITY CALL DATE TIME CODE INFO");
  }

  private static Pattern DATE_TIME_DESC = Pattern.compile("[\\*\n ]+(\\d{1,2}/\\d{1,2}/\\d{4}) (\\d{1,2}:\\d{2}:\\d{2})( [AP]M)? ProQA +");
  private static Pattern LEADING_ASTERISK = Pattern.compile(" *\\**(.*?)");
  private static Pattern TRAILING_ASTERISK = Pattern.compile("(.*?)\\** *");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CC911")) return false;

    // separate using Description tag
    int ni = body.indexOf("\nDescription:");
    if (ni == -1) return false;
    String head = body.substring(0, ni).trim();
    String desc = body.substring(ni + 13).trim();

    // ADDR CITY CALL
    Result res = parseAddress(StartType.START_ADDR, head);
    // If SAP can find a valid address or city then go ahead and parse, otherwise assign beginning to strCall
    if (res.getStatus() >= STATUS_INTERSECTION || res.getCity().length() > 0) {
      res.getData(data);
      data.strCall = res.getLeft();
    } else data.strCall = head;

    // DATE TIME
    Matcher mat = DATE_TIME_DESC.matcher(desc);
    if (mat.find()) {
      data.strDate = mat.group(1);
      if (mat.group(3) == null) data.strTime = mat.group(2);
      else setTime(TIME_FMT, mat.group(2) + mat.group(3), data);
    }

    //Split description by date time constructs
    String[] fields = DATE_TIME_DESC.split(desc);
    
    // remove leading and trailing \\*+ constructs from first and last fields
    mat = LEADING_ASTERISK.matcher(fields[0]);
    if (mat.matches()) fields[0] = mat.group(1);
    mat = TRAILING_ASTERISK.matcher(fields[fields.length-1]);
    if (mat.matches()) fields[fields.length-1] = mat.group(1);
    
    // parse Description fields to CODE CALL INFO
    for (String fld : fields) {
      if (fld.length() == 0) continue;

      // Nature: CODE - CALL
      if (fld.startsWith("Nature:")) {
        fld = fld.substring(7).trim();
        int hi = fld.indexOf(" - ");
        if (hi != -1) { 
          // parse CODE
          data.strCode = fld.substring(0, hi).trim();
          fld = fld.substring(hi + 3);
        }
        
        // if call and nature aren't identical (rare occasion) then append with " / "
        if (data.strCall.equals(fld)) continue;
        data.strCall = append(data.strCall, " / ", fld);
        continue;
      }

      // notes and unlabeled fields go in info
      if (fld.startsWith("Notes:")) fld = fld.substring(6).trim();
      data.strSupp = append(data.strSupp, "\n", fld);
    }

    // adjust cities
    if (data.strCity.equals("CHEROKEE COUNTY")) {
      data.strCity = "";
    } else if (data.strCity.equals("OUT OF COUNTY")) {
      data.strCity = "";
      data.defCity = "";
    }

    return true;
  }

  private static String[] CITY_LIST =new String[] {
      "CHEROKEE COUNTY", 
      "OUT OF COUNTY" 
  };
}
