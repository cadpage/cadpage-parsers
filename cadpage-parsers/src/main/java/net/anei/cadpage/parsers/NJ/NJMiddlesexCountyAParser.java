package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class NJMiddlesexCountyAParser extends SmartAddressParser {
  
  public NJMiddlesexCountyAParser() {
    super("MIDDLESEX COUNTY", "NJ");
    setFieldList("SRC CALL ADDR CITY APT UNIT X");
  }

  private static Pattern MASTER = Pattern.compile(".*?:(.*?):(?:([^-\\|]*?)- )?(.*?)(?:,(.*?))?(?:(?:\\|(.*?))?\\|(?:(.*?)X)?(.*?))?");
  private static Pattern CROSS_DELIMETER = Pattern.compile(" +TO +| *- *", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;
    data.strSource = subject;
    data.strCall = mat.group(1).trim();
    data.strCity = getOptGroup(mat.group(2));
    parseAddress(mat.group(3).trim(), data);
    data.strCity = append(data.strCity, " - ", getOptGroup(mat.group(4)));
    data.strApt = getOptGroup(mat.group(5));
    data.strUnit = getOptGroup(mat.group(6));
    String g7 = mat.group(7);
    if (g7 != null) data.strCross = CROSS_DELIMETER.matcher(g7.trim()).replaceAll(" & "); 
    return true;
  }
  
  private static Pattern REDUNDANT_MADDR = Pattern.compile("(.*?) & (\\d+ \\1)");
  
  @Override
  public String postAdjustMapAddress(String sAddress) {
    // take care of maddresses like "VINEYARD CT & 421 VINEYARD CT"
    Matcher mat = REDUNDANT_MADDR.matcher(sAddress);
    if (mat.matches()) sAddress = mat.group(2);
    return sAddress;
  }
}
