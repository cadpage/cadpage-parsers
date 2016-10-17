
package net.anei.cadpage.parsers.AZ;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;


public class AZYavapaiCountyBParser extends SmartAddressParser {
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("->Inc Addr = (.*?) Equip = (.*?) (.*?)(?: Rpt # = (\\S+))?");
  private static final Pattern RUN_REPORT_TIME_PTN = Pattern.compile("\\b(\\d\\d) (\\d\\d) (\\d\\d)");
  private static final Pattern RUN_REPORT_BRK_PTN = Pattern.compile(" (?=\\w+ =)");
  private static final Pattern MASTER = Pattern.compile("([A-Z0-9]{1,5}) *(?:\\d{3})? Utl# (\\d{2,4}(?: ?[A-Z]\\d?)?(?: [- A-Z0-9]+?)?|M H S) +(\\d\\d/\\d\\d/\\d\\d) +([-, A-Z0-9]+?)(?: *#([-,A-Z0-9]+))? / (.*?)[ /]+((?:[A-Z]+\\d+|[A-Z0-9]+FD)(?: ?, ?(?:[A-Z]+\\d+|[A-Z0-9]+FD))*)");
  private static final Pattern DELIM_PTN = Pattern.compile(" +[A-Z]{2,3}(?:-|/+|\\.{2,}) *(?:(?:APT|UNIT|#) *([A-Z0-9]+)[ /]+)?");
  private static final Pattern NT_PTN = Pattern.compile(" *\\bNT\\b *");

  public AZYavapaiCountyBParser() {
    super(CITY_CODES, "YAVAPAI COUNTY", "AZ");
  }
  
  @Override
  public String getFilter() {
    return "noreply@prescott-az.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("->Inc Addr =")) {
      setFieldList("ADDR UNIT INFO ID");
      data.msgType = MsgType.RUN_REPORT;
      Matcher match = RUN_REPORT_PTN.matcher(body);
      if (match.matches()) {
        parseAddress(match.group(1).trim(), data);
        data.strUnit = match.group(2).trim();
        String times = match.group(3).trim();
        times = RUN_REPORT_TIME_PTN.matcher(times).replaceAll("$1:$2:$3");
        times = RUN_REPORT_BRK_PTN.matcher(times).replaceAll("\n");
        times = times.replace(" 00 00", "");
        data.strSupp = times;
        data.strCallId = getOptGroup(match.group(4));
      } else {
        data.strSupp = body;
      }
      return true;
    }
    
    // Use master pattern to break out main fields
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    setFieldList("CODE MAP DATE ADDR APT CITY X INFO UNIT");
    
    data.strCode = match.group(1);
    data.strMap = match.group(2);
    data.strDate = match.group(3);
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(4).trim(), data);
    data.strAddress = NT_PTN.matcher(data.strAddress).replaceAll(" ").trim();
    String apt = match.group(5);
    if (apt != null) data.strApt = append(data.strApt, "-", apt);
    String extra = match.group(6).trim();
    data.strUnit = match.group(7).replace(" ", "");
    
    // What is left is the address and cross street and there may or
    // may not be a recognizable separator
    match = DELIM_PTN.matcher(extra);
    if (match.find()) {
      data.strCross = extra.substring(0,match.start());
      apt = match.group(1);
      if (apt != null) data.strApt = append(data.strApt, "-", apt);
      data.strSupp = extra.substring(match.end());
    }
    
    // If not, then we just have to hope the smart address parser can sort it out
    else {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_NO_CITY, extra, data);
      data.strSupp = getLeft();
    }
    
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CY", "YAVAPAI COUNTY",
      "CV", "CHINO VALLEY",
      "PR", "PRESCOTT",
      "PV", "PRESCOTT VALLEY",
      "GC", "GROOM CREEK",
      "WV", "WILLIAMSON VALLEY"

  });
}
