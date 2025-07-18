package net.anei.cadpage.parsers.LA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class LATangipahoaParishCParser extends MsgParser {

  public LATangipahoaParishCParser() {
    super("TANGIPAHOA PARISH", "LA");
    setFieldList("SRC CODE ADDR APT CITY ST CALL DATE TIME PLACE INFO");
  }

  @Override
  public String getFilter() {
    return "pointecoupee@pagingpts.com";
  }

  private static final Pattern SRC_CODE_PTN = Pattern.compile("([A-Z]+) - (\\S+)");
  private static final Pattern MASTER = Pattern.compile("([A-Z]+) +([^,]*)(?:, ([A-Za-z ]*))?, ([A-Z]{2}) (.*) (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d [AP]M)\\b *(.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("[A-Z, ]+ \\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d [AP]M: *(?:This message was sent by AutoPage from Page Manager: *)?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SRC_CODE_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    data.strCode = match.group(2);

    String sExtra = "";
    int pt = body.indexOf("\n\n");
    if (pt >= 0) {
      sExtra = body.substring(pt+2).trim();
      body = body.substring(0,pt).trim();
    }

    match = MASTER.matcher(body);
    if (!match.matches()) return false;

    if (!data.strSource.equals(match.group(1))) return false;

    String addr = match.group(2).trim();
    String apt = null;
    pt = addr.indexOf(" APT ");
    if (pt >= 0) {
      apt = addr.substring(pt+5).trim();
      addr = addr.substring(0, pt).trim();
    }
    parseAddress(addr, data);
    if (apt != null) data.strApt = append(data.strApt, "-", apt);

    data.strCity = getOptGroup(match.group(3));
    data.strState = match.group(4).trim();
    data.strCall = match.group(5).trim();
    data.strDate = match.group(6);
    setTime(TIME_FMT, match.group(7), data);
    data.strPlace = match.group(8);

    for (String line : sExtra.split("\n+")) {
      line = line.trim();
      match = INFO_HDR_PTN.matcher(line);
      if (match.lookingAt()) line =  line.substring(match.end());
      data.strSupp = append(data.strSupp, "\n", line);
    }
    return true;
  }
}
