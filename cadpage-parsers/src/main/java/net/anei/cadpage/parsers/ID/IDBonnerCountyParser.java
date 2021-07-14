package net.anei.cadpage.parsers.ID;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class IDBonnerCountyParser extends SmartAddressParser {

  public IDBonnerCountyParser() {
    super("BONNER COUNTY", "ID");
    setFieldList("SRC CALL ADDR X PLACE APT CITY INFO DATE TIME");
    removeWords("MALL", "STREET");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern MASTER = Pattern.compile("([A-Z]{3,4}) _([^_]+) _(.*)");
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*?), ([A-Z]{3}) +(.*)");
  private static final Pattern ADDR_SPLIT_PTN = Pattern.compile(";| - ");
  private static final Pattern APT_PTN = Pattern.compile("[A-Z]?\\d+[A-Z]?|(?:APT|RM|ROOM|#) *([^ ]+) *(.*)");
  private static final Pattern NEAR_PTN = Pattern.compile("NEAR:? +(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile(" *\\b(\\d\\d \\d\\d \\d\\d) (\\d\\d \\d\\d \\d{4})\\b[- ]*");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    data.strCall = match.group(2).trim();
    String addr = match.group(3).trim();

    addr = addr.replace('\n', ' ');
    String info = null;
    match = ADDR_CITY_PTN.matcher(addr);
    if (match.matches()) {
      addr = match.group(1).trim();
      data.strCity = convertCodes(match.group(2), CITY_CODES);
      info = match.group(3);
    }

    boolean first = true;
    for (String part : ADDR_SPLIT_PTN.split(addr)) {
      part = part.trim();
      if (first) {
        parseAddress(part, data);
        first = false;
      } else {
        match = APT_PTN.matcher(part);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt != null) {
            part = apt;
            data.strPlace = append(data.strPlace, " - ", match.group(2));
          }
          data.strApt = append(data.strApt, "-", part);
        } else {
          String tmp = part;
          match = NEAR_PTN.matcher(part);
          if (match.matches()) tmp = match.group(1);
          if (isValidAddress(tmp)) {
            data.strCross = append(data.strCross, " / ", tmp);
          } else {
            data.strPlace = append(data.strPlace, " - ", part);
          }
        }
      }
    }

    if (info != null) {
      match = INFO_DATE_TIME_PTN.matcher(info);
      int last = 0;
      while (match.find()) {
        if (data.strTime.length() == 0) {
          data.strTime = match.group(1).replace(' ', ':');
          data.strDate = match.group(2).replace(' ', '/');
        }
        data.strSupp = append(data.strSupp, "\n", info.substring(last,match.start()));
        last = match.end();
      }
      data.strSupp = append(data.strSupp, "\n", info.substring(last));
    }
    return true;

  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ATH", "ATHOL",
      "BLA", "BLANCHARD",
      "CLA", "CLARK FORK",
      "COC", "COCOLALLA",
      "COO", "COOLIN",
      "HOP", "HOPE",
      "OLD", "OLDTOWN",
      "PON", "PONDERAY",
      "PRI", "PRIEST RIVER",
      "PRL", "PRIEST LAKE",
      "SAN", "SANDPOINT",
      "SAG", "SAGLE"
  });
}
